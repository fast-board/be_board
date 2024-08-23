package com.example.fastboard.global.common.exception;

import com.example.fastboard.global.common.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BasicException.class)
    public ResponseEntity<ExceptionResponse> handleBasicException(BasicException e) {
        ExceptionResponse response = new ExceptionResponse(e);
        HttpStatus httpStatus = e.getErrorCode().getHttpStatus();

        return new ResponseEntity<>(response, httpStatus);
    }
}
