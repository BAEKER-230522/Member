package com.baeker.member.base.error.exception;

public class AccessTokenExpirationException extends RuntimeException {
    public AccessTokenExpirationException(String msg) {
        super(msg);
    }
}
