package com.baeker.member.base.error.exception.jwt;

public class JwtException extends RuntimeException {
    public JwtException(String msg) {
        super(msg);
    }
}
