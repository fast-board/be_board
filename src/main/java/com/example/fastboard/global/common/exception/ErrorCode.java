package com.example.fastboard.global.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getMessage();
    HttpStatus getHttpStatus();
    RuntimeException getException();
    RuntimeException getException(Throwable cause);
}
