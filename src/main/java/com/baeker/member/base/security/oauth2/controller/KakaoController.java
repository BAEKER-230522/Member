package com.baeker.member.base.security.oauth2.controller;

import com.baeker.member.base.security.jwt.JwtTokenProvider;
import com.baeker.member.base.security.oauth2.model.providers.ProviderUser;
import com.baeker.member.base.security.oauth2.service.CustomOAuth2UserService;
import com.baeker.member.base.security.oauth2.service.KakaoService;
import com.baeker.member.base.security.oauth2.users.dto.SocialLoginResponse;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao")
    public SocialLoginResponse kakaoLogin(String code, String redirectUri) {
        return kakaoService.kakaoLogin(code, redirectUri);
//        String name = oAuth2User.getName();
//        Member byUsername = memberService.findByUsername(name);
//        Map<String, String> mapToken = jwtTokenProvider.genAccessTokenAndRefreshToken(byUsername);
//        return new JwtTokenResponse(mapToken.get("accessToken"), mapToken.get("refreshToken"));
    }
}
