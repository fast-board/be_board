package com.example.fastboard.domain.wish.exception;

import com.example.fastboard.global.common.exception.BasicException;
import com.example.fastboard.global.common.exception.ErrorCode;

public class WishException extends BasicException {

    public WishException(ErrorCode errorCode) {
        super(errorCode);
    }

    public WishException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public WishException(ErrorCode errorCode, Object errorInfo) {
        super(errorCode, errorInfo);
    }

    public WishException(ErrorCode errorCode, Object errorInfo, Throwable cause) {
        super(errorCode, errorInfo, cause);
    }
}
