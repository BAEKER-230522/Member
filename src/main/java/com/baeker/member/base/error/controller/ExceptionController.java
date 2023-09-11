package com.baeker.member.base.error.controller;

import com.baeker.member.base.error.ErrorMsg;
import com.baeker.member.base.error.exception.jwt.AccessTokenExpirationException;
import com.baeker.member.base.error.exception.InvalidDuplicateException;
import com.baeker.member.base.error.exception.NotFoundException;
import com.baeker.member.base.error.exception.jwt.RefreshTokenExpirationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMsg> exceptionHandler(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorMsg(e.getMessage()));
    }

    @ExceptionHandler(InvalidDuplicateException.class)
    public ResponseEntity<ErrorMsg> exceptionHandler(InvalidDuplicateException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorMsg(e.getMessage()));
    }

    @ExceptionHandler(RefreshTokenExpirationException.class)
    public ResponseEntity<ErrorMsg> refreshTokenExpirationException(RefreshTokenExpirationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(new ErrorMsg(e.getMessage()));
    }

    @ExceptionHandler(AccessTokenExpirationException.class)
    public ResponseEntity<ErrorMsg> accessTokenExpirationException(AccessTokenExpirationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new ErrorMsg(e.getMessage()));
    }
}












