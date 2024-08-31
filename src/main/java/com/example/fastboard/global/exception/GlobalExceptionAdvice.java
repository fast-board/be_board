package com.example.fastboard.global.exception;

import com.example.fastboard.global.common.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseDTO<Object>> applicationException(ApplicationException e) {
        log.error("정의된 에러 : " + e.getErrorCode().getMessage(), e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ResponseDTO.errorWithMessageAndData(HttpStatus.BAD_REQUEST,e.getMessage(),e.getData()));
    }

    /**
     * @Valid 유효성 검사를 통과하지 못한 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(String.format("%s - %s; ", fieldError.getField(), fieldError.getDefaultMessage()));
            log.error("유효성 에러: " + errorMessage.toString(), fieldError);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.errorWithMessage(HttpStatus.BAD_REQUEST, errorMessage.toString()));
    }


    /**
     * DB 관련 기타 예외 처리
     */
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ResponseDTO<Void>> handleDataException(DataAccessException e) {
        log.error("DB 에러 : " + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> unhandledException(Exception e, HttpServletRequest request) {
        log.error("정의되지 않은 에러 : {} {} errMessage={}\n",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
