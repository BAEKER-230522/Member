package com.baeker.member.base.security.jwt;

import com.baeker.member.base.error.exception.jwt.RefreshTokenExpirationException;
import com.baeker.member.base.util.redis.RedisUt;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RedisUt redisUt;
    private final MemberService memberService;
    private final JwtTokenProvider jwtProvider;

//    public String doFilterInternal(String accessToken, String refreshToken) throws IOException {
//        String accessToken = request.getHeader("Authorization");
//
//        // accessToken 만료된 경우
//        if (accessToken == null) {
//            // 리프레시 토큰 확인
//            String refreshToken = request.getHeader("refreshToken");
//            if (refreshToken != null) {
//                return createNewAccessToken(refreshToken).get("accessToken");
//            }
//        } else {
//            if (jwtProvider.verify(accessToken)) {
//                Member member = null;
//                Map<String, Object> claims = jwtProvider.getClaims(accessToken);
//                long id = (int) claims.get("id");
//
//                member = memberService.findById(id);
//
//                forceAuthentication(member);
//            }
//        }
//        return "로그아웃";
//    }

    // 새로운 액세스 토큰 발급하는 메서드
    public JwtTokenResponse createNewAccessToken(String refreshToken) throws IOException {
        Member member = null;

        Map<String, Object> claims = jwtProvider.getClaims(refreshToken);

        long id = (int) claims.get("id");
        member = memberService.findById(id);
        String redisSelectId = ""+id;
        Long ttl = redisUt.getExpire(redisSelectId);

        // 리프레시 토큰까지 만료되었거나 키가 존재하지 않는 경우
        if (ttl < 0) {
            throw new RefreshTokenExpirationException();
        }

        return jwtProvider.genAccessTokenAndRefreshToken(member);
    }

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
