package com.example.fastboard.global.common.auth.exception;

import com.example.fastboard.global.common.exception.BasicException;
import com.example.fastboard.global.common.exception.ErrorCode;

public class AuthException extends BasicException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public AuthException(ErrorCode errorCode, Object errorInfo) {
        super(errorCode, errorInfo);
    }

    public AuthException(ErrorCode errorCode, Object errorInfo, Throwable cause) {
        super(errorCode, errorInfo, cause);
    }
}
