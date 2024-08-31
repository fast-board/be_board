package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class MemberException extends ApplicationException {

    public MemberException(ErrorCode errorCode){
        super(errorCode);
    }

    public MemberException(ErrorCode errorCode, Object data){
        super(errorCode,data);
    }
}
