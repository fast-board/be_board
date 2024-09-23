package com.example.fastboard.domain.board.exception;

import com.example.fastboard.global.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BoardErrorCode implements ErrorCode {
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String message;
    private final HttpStatus httpStatus;

    BoardErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public RuntimeException getException() {
        return new BoardException(this);
    }

    @Override
    public RuntimeException getException(Throwable cause) {
        return new BoardException(this, cause);
    }
}
