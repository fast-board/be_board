package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class MemberAlreadyException extends ApplicationException {

    public MemberAlreadyException(ErrorCode errorCode){
        super(errorCode);
    }
}
