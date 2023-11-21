package com.baeker.member.base.security.oauth2.controller;

import com.baeker.member.base.security.oauth2.service.KakaoService;
import com.baeker.member.base.security.oauth2.users.dto.SocialLoginDto;
import com.baeker.member.base.security.oauth2.users.dto.SocialLoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao")
    public SocialLoginResponse kakaoLogin(HttpServletResponse response, String code, String redirectUri) {
        SocialLoginDto dto = kakaoService.kakaoLogin(code, redirectUri);
        String accessToken = dto.accessToken();
        String refreshToken = dto.refreshToken();
        boolean baekJoonConnect = dto.isBaekJoonConnect();
        Long memberId = dto.memberId();
        String encodeRefreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
        Cookie refreshTokenCookie = new Cookie("RefreshToken", encodeRefreshToken);
        response.addCookie(refreshTokenCookie);
        return new SocialLoginResponse(accessToken, memberId, baekJoonConnect);
    }
}
