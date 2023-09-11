package com.baeker.member.base.security.oauth2.service;

import com.baeker.member.base.error.ErrorResponse;
import com.baeker.member.base.error.exception.jwt.JwtCreateException;
import com.baeker.member.base.security.jwt.JwtService;
import com.baeker.member.base.security.jwt.JwtTokenProvider;
import com.baeker.member.base.security.oauth2.model.enums.OAuth2Config;
import com.baeker.member.base.util.redis.RedisUt;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Provider;
import java.util.Map;

import static com.baeker.member.base.error.ErrorResponse.JWT_CREATE_EXCEPTION;
import static com.baeker.member.base.security.oauth2.model.enums.OAuth2Config.SocialType.KAKAO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {
    private final CustomOidcUserService oidcUserService;

    @Value("${spring.security.oauth2.client.registration.kakao.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private AuthorizationGrantType authorizationGrantType;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-authentication-method}")
    private ClientAuthenticationMethod clientAuthenticationMethod;
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.client.provider.kakao.jwk-set-uri}")
    private String jwkSetUri;
    @Value("${spring.security.oauth2.client.provider.kakao.issuer-uri}")
    private String issuerUri;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenResponse kakaoLogin(String code, String redirectUri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        String body = "grant_type=" + authorizationGrantType.getValue() + "&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&code=" + code + "&client_secret=" + clientSecret;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        String response = restTemplate.postForObject(tokenUri, request, String.class);
        log.info("카카오 로그인 response : {}", response);
        JwtTokenResponse jwtTokenResponse = null;
        try {
            jwtTokenResponse = getOidcTokenId(response, redirectUri);
//            loginOidc(response, redirectUri);
        } catch (JsonProcessingException e) {
            throw new JwtCreateException(JWT_CREATE_EXCEPTION.getMessage());
        }
        return jwtTokenResponse;
    }


    private JwtTokenResponse getOidcTokenId(String response, String redirectUri) throws JsonProcessingException {
        JSONObject jsonObject = Json.mapper().readValue(response, JSONObject.class);
        String idToken = jsonObject.get("id_token").toString();
        String oauth2TokenId = jsonObject.get("access_token").toString();
        String scopes = jsonObject.get("scope").toString();


        OidcIdToken oidcIdToken = OidcIdToken.withTokenValue(idToken)
                .tokenValue(idToken)
                .build();
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, oauth2TokenId, null, null);
        String[] scope = scopeParsing(scopes);
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(KAKAO.getSocialName())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(authorizationGrantType)
                .redirectUri(redirectUri)
                .scope(scope)
                .tokenUri(tokenUri)
                .clientName(KAKAO.getSocialName())
                .userNameAttributeName("sub")
                .jwkSetUri(jwkSetUri)
                .userInfoUri(userInfoUri)
                .issuerUri(issuerUri)
                .clientAuthenticationMethod(clientAuthenticationMethod)
                .authorizationUri(authorizationUri)
                .build();
        OidcUserRequest oidcUserRequest = new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);
        Member byUsername = memberService.findByUsername(oidcUser.getName());
        return jwtTokenProvider.genAccessTokenAndRefreshToken(byUsername);
    }

    private void loginOidc(String response, String redirectUri) throws JsonProcessingException {
        JSONObject jsonObject = Json.mapper().readValue(response, JSONObject.class);
        String idToken = jsonObject.get("id_token").toString();
        String oauth2TokenId = jsonObject.get("access_token").toString();
        String scopes = jsonObject.get("scope").toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + idToken);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> forEntity = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, JSONObject.class);
        String name = forEntity.getBody().get("name").toString();
        String nickname = forEntity.getBody().get("nickname").toString();
        System.out.println("name = " + name);
        System.out.println("nickname = " + nickname);

    }

    private String[] scopeParsing(String scope) {
        return scope.split(" ");
    }
}
