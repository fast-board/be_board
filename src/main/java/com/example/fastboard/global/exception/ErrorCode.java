package com.example.fastboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //MEMBER
    MEMBER_DELETED_EXCEPTION(HttpStatus.NOT_FOUND, "삭제된 회원입니다."),
    MEMBER_ALREADY_REGISTERED_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 존재하는 회원 이메일 입니다."),
    MEMBER_NICKNAME_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"이미 존재하는 닉네임 입니다."),
    MEMBER_PHONE_NUMBER_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"이미 존재하는 전화번호 입니다."),


    //500 error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");


    private final HttpStatus httpStatus;
    private final String message;
}
