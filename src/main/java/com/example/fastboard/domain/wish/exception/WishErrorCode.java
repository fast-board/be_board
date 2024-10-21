package com.example.fastboard.domain.wish.exception;

import com.example.fastboard.global.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum WishErrorCode implements ErrorCode {
    WISH_NOT_FOUND("해당 좋아요가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    WISH_ALEADY_EXISTS("이미 해당 게시글을 좋아하고 있습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    WishErrorCode(String message, HttpStatus status) {
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
        return new WishException(this);
    }

    @Override
    public RuntimeException getException(Throwable cause) {
        return new WishException(this, cause);
    }
}
