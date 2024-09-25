package com.example.fastboard.domain.board.exception;

import com.example.fastboard.global.common.exception.BasicException;
import com.example.fastboard.global.common.exception.ErrorCode;

public class BoardException extends BasicException {
    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BoardException(ErrorCode errorCode, Object errorInfo) {
        super(errorCode, errorInfo);
    }

    public BoardException(ErrorCode errorCode, Object errorInfo, Throwable cause) {
        super(errorCode, errorInfo, cause);
    }
}
