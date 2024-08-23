package com.example.fastboard.global.common.response;


import com.example.fastboard.global.common.exception.BasicException;
import com.example.fastboard.global.common.exception.ErrorCode;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ExceptionResponse {
    private String name;
    private String message;
    private Integer status;
    private Instant timestamp;

    public ExceptionResponse(BasicException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String errorName = exception.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);

        this.name = errorName;
        this.status = errorCode.getHttpStatus().value();
        this.message = exception.getMessage();
        this.timestamp = Instant.now();
    }
}
