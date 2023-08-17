package com.baeker.member.base.exception;

public class RefreshTokenExpirationException extends RuntimeException {
    public RefreshTokenExpirationException() {
        super("refreshToken이 만료되었습니다.");
    }
}
