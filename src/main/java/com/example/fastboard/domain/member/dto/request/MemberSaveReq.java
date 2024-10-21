package com.example.fastboard.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record MemberSaveReq(

        @Email(message = "이메일 형식을 지켜주세요.")
        @NotNull(message = "이메일을 입력해주세요.")
        String email,

        @NotBlank(message = "이름을 입력해주세요.")
        @NotNull(message = "이름을 입력해주세요.")
        String name,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @NotNull(message = "비밀번호를 입력해주세요.")
        String password,

        String nickname,

        String phoneNumber
) {}
