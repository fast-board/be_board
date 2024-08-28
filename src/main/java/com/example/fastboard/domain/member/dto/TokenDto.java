package com.example.fastboard.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenDto {

    private String grantType;  // JWT에 대한 인증 타입 (Bearer을 사용)
    private String accessToken;
    private String refreshToken;
}