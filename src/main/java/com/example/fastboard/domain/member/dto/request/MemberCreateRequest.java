package com.example.fastboard.domain.member.dto.request;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberCreateRequest(
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String name,
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        String nickname,
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        String phoneNumber,
        @Email
        String email,
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        String password
) {

    public Member toEntity(String encryptedPassword) {
        return Member.builder()
                .name(name)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .email(email)
                .encryptedPassword(encryptedPassword)
                .role(Role.USER)
                .build();
    }
}
