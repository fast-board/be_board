package com.example.fastboard.domain.member.exception;


import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class MemberNotFoundException extends ApplicationException {

    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode,null);
    }
}
