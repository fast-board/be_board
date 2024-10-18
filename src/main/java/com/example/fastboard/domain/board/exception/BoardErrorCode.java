package com.example.fastboard.domain.board.exception;

import com.example.fastboard.global.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BoardErrorCode implements ErrorCode {
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_NOT_FOUND("이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_EQUAL("게시글의 작성자가 압니니다.", HttpStatus.BAD_REQUEST),
    BOARD_NOT_FOUND("해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_COMMENT("해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


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
