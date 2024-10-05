package com.example.fastboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //MEMBER
    MEMBER_ALREADY_REGISTERED_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 존재하는 회원 이메일 입니다."),
    MEMBER_NICKNAME_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"이미 존재하는 닉네임 입니다."),
    MEMBER_PHONE_NUMBER_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"이미 존재하는 전화번호 입니다."),
    MEMBER_DELETED_EXCEPTION(HttpStatus.NOT_FOUND, "삭제된 회원입니다."),
    MEMBER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"존재하지 않는 회원 입니다."),

    //AUTH
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    AUTHORIZATION_HEADER_MUST_START_BEARER_EXCEPTION(HttpStatus.BAD_REQUEST,"Authorization Header가 bearer로 시작하지 않습니다"),
    UNAUTHORIZATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증 정보가 필요합니다."),
    AUTHOR_MISMATCH_EXCEPTION(HttpStatus.FORBIDDEN,"해당 게시글에 대한 권한이 없습니다."),

    //TOKEN
    INVALID_TOKEN_SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST,"잘못된 JWT 서명입니다. (구조적 문제)" ),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    NOT_EXPIRED_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰이 만료되지 않았습니다."),
    UNSUPPORTED_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 형식 입니다."),
    ILLEGAL_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST,"JWT 토큰이 Null이거나 비어있습니다."),
    MISSING_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰이 요청 헤더에 포함되어 있지 않습니다."),
    TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST,"권한정보가 없는 토큰 입니다."),
    REFRESHTOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"refreshToken이 존재하지 않습니다."),

    //BOARD
    BOARD_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"게시글을 찾을 수 없습니다."),
    BOARD_DELETED_EXCEPTION(HttpStatus.NOT_FOUND, "삭제된 게시글입니다."),


    //FILE
    FILE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"파일이 존재하지 않습니다."),
    FILE_SIZE_LIMIT_EXCEPTION(HttpStatus.BAD_REQUEST,"파일 용량이 10MB를 초과할 수 없습니다." ),
    FILE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"이미 동일한 디렉토리 또는 파일 존재"),
    PATH_NOT_FOUND(HttpStatus.BAD_REQUEST, "(Path)경로가 존재하지 않습니다."),
    FILE_ORIGINAL_NAME_IS_EMPTY_EXCEPTION(HttpStatus.BAD_REQUEST,"원본 파일 이름이 NULL입니다."),
    FILE_IOEXCEPTION(HttpStatus.BAD_REQUEST,"알 수 없는 이유로 처리에 실패 하였습니다."),

    //BOARD_IMAGE
    IMAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"이미지를 찾을 수 없습니다."),

    //500 error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");


    private final HttpStatus httpStatus;
    private final String message;
}
