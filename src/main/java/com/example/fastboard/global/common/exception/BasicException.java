package com.example.fastboard.global.common.exception;

public class BasicException extends RuntimeException {
    protected ErrorCode ERROR_CODE;
    protected Object data;

    public BasicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.ERROR_CODE = errorCode;
    }

    public BasicException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.ERROR_CODE = errorCode;
    }

    public BasicException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.ERROR_CODE = errorCode;
        this.data = data;
    }

    public BasicException(ErrorCode errorCode, Object data, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.ERROR_CODE = errorCode;
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return ERROR_CODE;
    }

    public Object getData() {
        return data;
    }
}
