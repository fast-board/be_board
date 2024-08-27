package com.example.fastboard.global.common.exception;

import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse> handleMemberException(MemberException e) {
        ApiResponse response = new ApiResponse(e.getErrorCode().getHttpStatus().value(), e.getMessage(), e.getErrorInfo());
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiResponse response = new ApiResponse(httpStatus.value(), e.getBindingResult().getFieldError().getDefaultMessage(), null );

        return new ResponseEntity<>(response, httpStatus);
    }
}
