package com.example.fastboard.global.common;

import com.example.fastboard.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ResponseDTO<T> {
    private final int status;
    private final T data;
    private final String message;


    public static ResponseDTO<Void> ok() {
        return ResponseDTO.<Void>builder()
                .status(HttpStatus.OK.value())
                .data(null)
                .message(null)
                .build();
    }

    public static <T> ResponseDTO<T> okWithData(T data) {
        return ResponseDTO.<T>builder()
                .status(HttpStatus.OK.value())
                .data(data)
                .message(null)
                .build();
    }

    public static <T> ResponseDTO<T> okWithMessageAndData(String message, T data){
        return ResponseDTO.<T>builder()
                .status(HttpStatus.OK.value())
                .data(data)
                .message(message)
                .build();
    }

    public static ResponseDTO<Void> error(ErrorCode errorCode) {
        return ResponseDTO.<Void>builder()
                .status(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    public static ResponseDTO<Void> errorWithMessage(HttpStatus httpStatus, String errorMessage) {
        return ResponseDTO.<Void>builder()
                .status(httpStatus.value())
                .message(errorMessage)
                .data(null)
                .build();
    }
}
