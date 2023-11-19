package com.baeker.member.base.security.oauth2.controller;

import com.baeker.member.base.security.oauth2.service.KakaoService;
import com.baeker.member.base.security.oauth2.users.dto.SocialLoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao")
    public void kakaoLogin(HttpServletResponse response, String code, String redirectUri) {
        SocialLoginResponse socialLoginResponse = kakaoService.kakaoLogin(code, redirectUri);
        String accessToken = socialLoginResponse.accessToken();
        String refreshToken = socialLoginResponse.refreshToken();
        Cookie accessTokenCookie = new Cookie("Authorization", "Bearer " + accessToken);
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
