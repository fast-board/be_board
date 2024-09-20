package com.example.fastboard.domain.board.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class FileException extends ApplicationException {
    public FileException(ErrorCode errorCode){
        super(errorCode);
    }
    public FileException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
