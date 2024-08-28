package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class NoAuthoritiesException extends ApplicationException {

    public NoAuthoritiesException() {
        super(ErrorCode.NO_AUTHORITIES_EXCEPTION);
    }
}
