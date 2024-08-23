package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {
    NICKNAME_ALREADY_EXISTS("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS("이미 등록된 번호입니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;

    MemberErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
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
        return new MemberException(this);
    }

    @Override
    public RuntimeException getException(Throwable cause) {
        return new MemberException(this, cause);
    }
}
