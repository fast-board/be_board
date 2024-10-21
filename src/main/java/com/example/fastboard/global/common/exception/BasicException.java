package com.example.fastboard.global.common.exception;

public class BasicException extends RuntimeException {
    protected ErrorCode ERROR_CODE;
    protected Object errorInfo;

    public BasicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.ERROR_CODE = errorCode;
    }

    public BasicException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.ERROR_CODE = errorCode;
    }

    public BasicException(ErrorCode errorCode, Object errorInfo) {
        super(errorCode.getMessage());
        this.ERROR_CODE = errorCode;
        this.errorInfo = errorInfo;
    }

    public BasicException(ErrorCode errorCode, Object errorInfo, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.ERROR_CODE = errorCode;
        this.errorInfo = errorInfo;
    }

    public ErrorCode getErrorCode() {
        return ERROR_CODE;
    }

    public Object getErrorInfo() {
        return errorInfo;
    }
}

