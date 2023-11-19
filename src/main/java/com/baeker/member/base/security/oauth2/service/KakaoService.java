package com.baeker.member.base.security.oauth2.service;

import com.baeker.member.base.error.exception.jwt.JwtCreateException;
import com.baeker.member.base.security.jwt.JwtTokenProvider;
import com.baeker.member.base.security.oauth2.users.dto.SocialLoginDto;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;

import static com.baeker.member.base.error.ErrorResponse.JWT_CREATE_EXCEPTION;
import static com.baeker.member.base.security.oauth2.model.enums.OAuth2Config.SocialType.KAKAO;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService{
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

    public SocialLoginDto kakaoLogin(String code, String redirectUri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        String body = "grant_type=" + authorizationGrantType.getValue() + "&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&code=" + code + "&client_secret=" + clientSecret;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        String response = restTemplate.postForObject(tokenUri, request, String.class);
        log.info("카카오 로그인 response : {}", response);
        SocialLoginDto socialLoginDto = null;
        try {
            socialLoginDto = getOidcTokenId(response, redirectUri);

        } catch (JsonProcessingException | ParseException e) {
            throw new JwtCreateException(JWT_CREATE_EXCEPTION.getMessage());
        }
        return socialLoginDto;
    }


    private SocialLoginDto getOidcTokenId(String response, String redirectUri) throws JsonProcessingException, ParseException {
        JSONObject jsonObject = Json.mapper().readValue(response, JSONObject.class);
        String idToken = jsonObject.get("id_token").toString();
        String oauth2TokenId = jsonObject.get("access_token").toString();
        String scopes = jsonObject.get("scope").toString();
        JWT parse = JWTParser.parse(idToken);
        String subject = parse.getJWTClaimsSet().getSubject();
        OidcIdToken oidcIdToken = OidcIdToken.withTokenValue(idToken)
                .tokenValue(idToken)
                .subject(subject)
                .build();
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, oauth2TokenId, null, null);
        String[] scope = scopeParsing(scopes);
        ClientRegistration clientRegistration = setClientRegistration(redirectUri, scope);

        OidcUserRequest oidcUserRequest = new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);
        Member byUsername = memberService.findByUsername(oidcUser.getName());
        JwtTokenResponse token = jwtTokenProvider.genAccessTokenAndRefreshToken(byUsername);
        boolean baekJoonConnect = byUsername.getBaekJoonName() != null;
        return new SocialLoginDto(token.accessToken(), token.refreshToken(), byUsername.getId(), baekJoonConnect);
    }

    private ClientRegistration setClientRegistration(String redirectUri, String[] scope) {
        return ClientRegistration.withRegistrationId(KAKAO.getSocialName())
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
    }

    private String[] scopeParsing(String scope) {
        return scope.split(" ");
    }
}
