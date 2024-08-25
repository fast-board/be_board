package com.example.fastboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;

    protected ApplicationException(ErrorCode errorCode,String message){
        super(message);
        this.errorCode=errorCode;
    }

    @Override
    public String getMessage() {
        if(errorCode.getMessage()!=null){
            return errorCode.getMessage();
        }
        return super.getMessage();
    }
}
