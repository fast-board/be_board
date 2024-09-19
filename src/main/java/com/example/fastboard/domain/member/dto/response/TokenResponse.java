package com.example.fastboard.domain.member.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
        String grantType,
        String accessToken,
        String refreshToken
) {

}