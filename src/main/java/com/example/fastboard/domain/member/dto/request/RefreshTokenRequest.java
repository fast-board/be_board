package com.example.fastboard.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest (
    @NotBlank(message = "공백일 수 없습니다.")
    @NotNull(message = "공백일 수 없습니다.")
    String refreshToken
){

}
