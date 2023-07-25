package com.baeker.member.base.security.handler;

import com.baeker.member.base.security.jwt.JwtTokenProvider;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${custom.front.url}")
    private String FRONT_URL;
    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;
//    private final RedisUt redisUt;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("OAuth2AuthenticationSuccessHandler");
        Member member = null;
        OidcUser user = (OidcUser) authentication.getPrincipal();
        member = memberService.findByUsername(user.getName());

        Map<String, String> tokens = jwtTokenProvider.genAccessTokenAndRefreshToken(member);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        Long memberId = member.getId();
        String url = FRONT_URL + "/login" +"?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8) + "&memberId=" + memberId;

//        String url = FRONT_URL;
//        response.addHeader("Authorization", "Bearer " + accessToken);
        response.sendRedirect(url);
    }
}
