package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class InvalidPasswordException extends ApplicationException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}
