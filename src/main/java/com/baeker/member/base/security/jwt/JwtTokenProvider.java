package com.baeker.member.base.security.jwt;

import com.baeker.member.base.util.Ut;
import com.baeker.member.base.util.redis.RedisUt;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
//    @Autowired
    private final RedisUt redisUt;
    private SecretKey cachedSecretKey;

    public final static long ACCESS_TOKEN_VALIDATION_SECOND = (1000L * 60 * 10); // 10분

    public final static long REFRESH_TOKEN_VALIDATION_SECOND = (1000L * 60 * 60) * 24; // 1일

    @Value("${custom.jwt.secret-key}")
    private String secretKeyPlain;

    private SecretKey _getSecretKey() {
        // 키를 Base64 인코딩함
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        // 인코딩된 키를 이용하여 SecretKey 객체 생성
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    // 가지고 있는 secretKey 가 없으면 생성, 있으면 해당 값 반환
    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();

        return cachedSecretKey;
    }

    // 지금으로부터 ttl 만큼의 유효기간 가지는 토큰 생성
    public String genToken(Map<String, Object> claims, long ttl) {
        long now = new Date().getTime();

        Date accessTokenExpiresIn = new Date(now + ttl);

        return Jwts.builder()
                .claim("body", Ut.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰 검증
    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) { // 액세스토큰 만료된 경우
            throw e;
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // 토큰으로부터 클레임을 가져온다
    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return Ut.json.toMap(body);
    }

    public JwtTokenResponse genAccessTokenAndRefreshToken(Member member) {
        String accessToken = genToken(member.toClaims(), ACCESS_TOKEN_VALIDATION_SECOND);
        String refreshToken = genToken(member.toClaims(), REFRESH_TOKEN_VALIDATION_SECOND);
        String key = String.valueOf(member.getId());
        redisUt.setValue(key, refreshToken, REFRESH_TOKEN_VALIDATION_SECOND);
        return new JwtTokenResponse(accessToken, refreshToken);
    }
}
