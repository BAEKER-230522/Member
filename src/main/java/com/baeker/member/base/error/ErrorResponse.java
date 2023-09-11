package com.baeker.member.base.error;

import lombok.Getter;

@Getter
public enum ErrorResponse {
    JWT_CREATE_EXCEPTION("JWT 생성 중 오류가 발생하였습니다.", 500),
    JWT_INVALID_EXCEPTION("JWT 토큰이 유효하지 않습니다.", 401),
    JWT_ACCESS_TOKEN_EXPIRATION_EXCEPTION("JWT Access 토큰이 만료되었습니다.", 401),
    JWT_REFRESH_TOKEN_EXPIRATION_EXCEPTION("JWT Refresh 토큰이 만료되었습니다.", 403),;
    private final String message;
    private final int status;

    ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
