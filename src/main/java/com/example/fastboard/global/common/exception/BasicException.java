package com.example.fastboard.global.common.exception;

public class BasicException extends RuntimeException {
    protected ErrorCode ERROR_CODE;

    public BasicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.ERROR_CODE = errorCode;
    }

    public BasicException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.ERROR_CODE = errorCode;
    }

    public ErrorCode getErrorCode() {
        return ERROR_CODE;
    }
}
