package com.example.fastboard.domain.member.dto.request;

import com.example.fastboard.domain.member.dto.parameter.MemberLoginParam;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record MemberLoginReq(
        @Email(message = "이메일 형식을 지켜주세요.")
        @NotNull(message = "이메일을 입력해주세요.")
        String email,

        @NotNull(message = "비밀번호를 입력해주세요.")
        String password
) {
    public MemberLoginParam toMemberLoginParam() {
        return new MemberLoginParam(this.email, this.password);
    }
}