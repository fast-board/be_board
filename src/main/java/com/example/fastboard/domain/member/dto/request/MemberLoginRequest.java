package com.example.fastboard.domain.member.dto.request;

public record MemberLoginRequest(
    String email,
    String password
) {

}
