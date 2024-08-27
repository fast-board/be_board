package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberLoginParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.common.auth.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberLoginTests {

    @Mock
    MemberRepository memberRepository;



    @InjectMocks
    MemberLoginService memberLoginService;

    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Spy
    JwtService jwtService = new JwtService("SecretKeySizeMustHaveSizeBigger256BitsSoSecretKeyLengthLongForUnitTest");



    public MemberLoginParam memberLoginParam() {
        return MemberLoginParam.builder()
                .email("TestEmail@email.com")
                .password("TestPassword")
                .build();
    }

    public Member member() {
        return Member.builder()
                .id(1L)
                .name("TestName")
                .email("TestEmail@email.com")
                .nickname("TestNickname")
                .phoneNumber("TestPhoneNumber")
                .role(Role.USER)
                .encryptedPassword(passwordEncoder.encode("TestPassword"))
                .build();
    }

    @Test
    @DisplayName("로그인 성공")
    public void login() {
        // given
        MemberLoginParam memberLoginParam = memberLoginParam();
        Member member = member();
        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(member));

        // when
        String token = memberLoginService.loginMember(memberLoginParam);

        // then
        Assertions.assertEquals(jwtService.getMemberId(token), member.getId());
        Assertions.assertEquals(jwtService.getRole(token), member.getRole().getRoleName());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 불일치")
    public void loginEmailNotFount() {

    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void loginPasswordNotEqual() {

    }
}
