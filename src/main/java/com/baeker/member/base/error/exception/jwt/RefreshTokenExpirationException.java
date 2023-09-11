package com.baeker.member.base.error.exception.jwt;

public class RefreshTokenExpirationException extends JwtException {
    public RefreshTokenExpirationException() {
        super("refreshToken이 만료되었습니다.");
    }
}
