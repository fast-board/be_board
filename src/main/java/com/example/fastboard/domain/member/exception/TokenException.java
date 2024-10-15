package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class TokenException extends ApplicationException {

    public TokenException(ErrorCode errorCode){
        super(errorCode);
    }

    public TokenException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
