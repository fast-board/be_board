package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.common.exception.BasicException;
import com.example.fastboard.global.common.exception.ErrorCode;

public class MemberException extends BasicException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public MemberException(ErrorCode errorCode, Object data, Throwable cause) {
        super(errorCode, data, cause);
    }

    public MemberException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
