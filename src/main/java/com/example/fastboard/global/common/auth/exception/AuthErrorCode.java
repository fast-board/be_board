package com.example.fastboard.global.common.auth.exception;

import com.example.fastboard.global.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {

    IS_NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다. (로그인이 필요합니다.)"),
    IS_NOT_VALID_AUTHORIZE(HttpStatus.FORBIDDEN, "해당 권한이 존재하지 않습니다.");

    private HttpStatus status;
    private String message;

    AuthErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

    @Override
    public RuntimeException getException() {
        return new AuthException(this);
    }

    @Override
    public RuntimeException getException(Throwable cause) {
        return new AuthException(this, cause);
    }
}
