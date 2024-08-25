package com.example.fastboard.domain.member.exception;

import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;

public class MemberDeletedException extends ApplicationException {
    public MemberDeletedException() {
        super(ErrorCode.MEMBER_DELETED_EXCEPTION);
    }
}
