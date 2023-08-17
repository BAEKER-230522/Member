package com.baeker.member.base.security.jwt;

import com.baeker.member.base.security.cookie.CookieUt;
import com.baeker.member.base.util.redis.RedisUt;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;
    private final MemberService memberService;
//    private final CookieUt cookieUt;
    private final RedisUt redisUt;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        // accessToken 만료된 경우
        if (accessToken == null) {
            // 리프레시 토큰 확인
            String refreshToken = request.getHeader("refreshToken");
            if (refreshToken != null) {
                createNewAccessToken(refreshToken, response);
            }
        } else {
            if (jwtProvider.verify(accessToken)) {
                Member member = null;
                Map<String, Object> claims = jwtProvider.getClaims(accessToken);
                long id = (int) claims.get("id");

                member = memberService.findById(id);

                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 새로운 액세스 토큰 발급하는 메서드
    private void createNewAccessToken(String refreshToken, HttpServletResponse response) throws IOException {
        Member member = null;
        log.debug("토큰 만료");
        Map<String, Object> claims = jwtProvider.getClaims(refreshToken);

        long id = (int) claims.get("id");
        member = memberService.findById(id);

        Long ttl = redisUt.getExpire(id);

        // 리프레시 토큰까지 만료되었거나 키가 존재하지 않는 경우
        if (ttl < 0) {
            log.debug("재로그인");
            response.sendRedirect("/oauth2/authorization/kakao");
        }

        Map<String, String> tokens = jwtProvider.genAccessTokenAndRefreshToken(member);
        response.addHeader("accessToken", tokens.get("accessToken"));
    }

    // 강제로 로그인 처리하는 메서드 (로그인한 사용자의 정보를 가져옴)
    private void forceAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());
        // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(user, null, member.getAuthorities());

        // 스프링 시큐리티 내에 authentication 객체를 저장할 context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // context 에 유저정보 저장
        context.setAuthentication(authentication);
        // 스프링 시큐리티에 context 등록
        SecurityContextHolder.setContext(context);
    }
}
