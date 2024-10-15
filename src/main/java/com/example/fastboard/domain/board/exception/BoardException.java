package com.example.fastboard.domain.board.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class BoardException extends ApplicationException {

    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
