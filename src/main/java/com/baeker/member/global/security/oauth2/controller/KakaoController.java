package com.baeker.member.global.security.oauth2.controller;

import com.baeker.member.global.security.oauth2.service.KakaoService;
import com.baeker.member.global.security.oauth2.users.dto.SocialLoginResponse;
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
    public SocialLoginResponse kakaoLogin(String code, String redirectUri) {
        return kakaoService.kakaoLogin(code, redirectUri);
    }
}
