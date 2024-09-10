package com.example.fastboard.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberReissueReq(
        @NotBlank(message = "Refresh Token 을 입력해주세요.")
        String refreshToken) {

}
