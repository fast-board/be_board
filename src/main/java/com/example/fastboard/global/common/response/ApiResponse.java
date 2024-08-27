package com.example.fastboard.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;
}
