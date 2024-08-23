package com.example.fastboard.domain;

import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.exception.MemberErrorCode;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.domain.member.service.MemberSaveService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberTests {

    @Mock
    MemberRepository memberRepository;


    @InjectMocks
    MemberSaveService memberSaveService;

    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MemberSaveParam memberSaveParam() {
        return MemberSaveParam.builder()
                .name("TestName")
                .email("TestEmail@email.com")
                .nickname("TestNickname")
                .phoneNumber("TestPhoneNumber")
                .password("TestPassword")
                .build();
    }

    @Test
    @DisplayName("유저 저장 - 성공 케이스")
    public void saveUser() {

        // given
        Member member = Member.builder()
                .id(1L)
                .name("TestName")
                .email("TestEmail@email.com")
                .nickname("TestNickname")
                .phoneNumber("TestPhoneNumber")
                .role(Role.USER)
                .encryptedPassword(passwordEncoder.encode("TestPassword"))
                .build();

        MemberSaveParam memberSaveParam = MemberSaveParam.builder()
                .name("TestName")
                .email("TestEmail@email.com")
                .nickname("TestNickname")
                .phoneNumber("TestPhoneNumber")
                .password("TestPassword")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // when
        Member savedMember = memberSaveService.addMember(memberSaveParam);

        // then
        Assertions.assertNotNull(savedMember.getId());
        Assertions.assertEquals(member.getName(), savedMember.getName());
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
        Assertions.assertEquals(member.getNickname(), savedMember.getNickname());
        Assertions.assertEquals(member.getPhoneNumber(), savedMember.getPhoneNumber());

    }


    @Test
    @DisplayName("유저 저장 - 이메일 중복")
    public void saveDuplicatedUser() {
        // given
        MemberSaveParam memberSaveParam = memberSaveParam();
        when(memberRepository.existsByEmail(memberSaveParam.getEmail())).thenReturn(true); // 이메일이 중복되는 상황 가정.

        // when & then
        Assertions.assertThrows(new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS).getClass(), () -> memberSaveService.addMember(memberSaveParam));

        verify(memberRepository, never()).save(any(Member.class)); // Save 가 한번도 호출되지 않음.
    }
}
