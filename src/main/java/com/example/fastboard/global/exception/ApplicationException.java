package com.example.fastboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;
    private final Object data;

    protected ApplicationException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode=errorCode;
        this.data=null;
    }

    @Override
    public String getMessage() {
        if(errorCode.getMessage()!=null){
            return errorCode.getMessage();
        }
        return super.getMessage();
    }
}
