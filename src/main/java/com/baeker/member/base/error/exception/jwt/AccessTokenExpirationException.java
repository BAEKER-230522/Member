package com.baeker.member.base.error.exception.jwt;

public class AccessTokenExpirationException extends JwtException {
    public AccessTokenExpirationException(String msg) {
        super(msg);
    }
}
