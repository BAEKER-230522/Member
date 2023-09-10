package com.baeker.member.base.security.oauth2.service;

import com.baeker.member.member.in.resDto.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Provider;

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
    private String authorizationGrantType;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    public void kakaoLogin(String code, String redirectUri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        String body = "grant_type=" + authorizationGrantType + "&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&code=" + code + "&client_secret=" + clientSecret;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        String response = restTemplate.postForObject(tokenUri, request, String.class);
        log.info("카카오 로그인 response : {}", response);
    }
}
