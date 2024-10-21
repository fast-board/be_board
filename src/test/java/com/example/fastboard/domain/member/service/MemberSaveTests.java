package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.exception.MemberErrorCode;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberSaveTests {

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
        Assertions.assertEquals(member.getName(), savedMember.getName());
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
        Assertions.assertEquals(member.getNickname(), savedMember.getNickname());
        Assertions.assertEquals(member.getPhoneNumber(), savedMember.getPhoneNumber());

    }


    @Test
    @DisplayName("유저 저장 실패 - 이메일 중복")
    public void saveEmailDuplicatedUser() {
        // given
        MemberSaveParam memberSaveParam = memberSaveParam();
        Member member = Member.builder()
                .name(memberSaveParam.getName())
                .email(memberSaveParam.getEmail())
                .nickname(memberSaveParam.getNickname() + "salt")
                .phoneNumber(memberSaveParam.getPhoneNumber() + "salt")
                .encryptedPassword(memberSaveParam().getPassword())
                .role(Role.USER)
                .build();
        when(memberRepository.findFirstByEmailOrNicknameOrPhoneNumber(memberSaveParam.getEmail(), memberSaveParam.getNickname(), memberSaveParam().getPhoneNumber()))
                .thenReturn(Optional.ofNullable(member)); // 이메일이 중복되는 상황 가정.

        // when & then
        MemberException memberException = Assertions.assertThrows(new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS).getClass(), () -> memberSaveService.addMember(memberSaveParam));
        Assertions.assertEquals(MemberErrorCode.EMAIL_ALREADY_EXISTS, memberException.getErrorCode());

        verify(memberRepository, never()).save(any(Member.class)); // Save 가 한번도 호출되지 않음.
    }


    @Test
    @DisplayName("유저 저장 실패 - 닉네임 중복")
    public void saveNickNameDuplicatedUser() {
        // given
        MemberSaveParam memberSaveParam = memberSaveParam();
        Member member = Member.builder()
                .name(memberSaveParam.getName())
                .email(memberSaveParam.getEmail() + "salt")
                .nickname(memberSaveParam.getNickname())
                .phoneNumber(memberSaveParam.getPhoneNumber() + "salt")
                .encryptedPassword(memberSaveParam().getPassword())
                .role(Role.USER)
                .build();
        when(memberRepository.findFirstByEmailOrNicknameOrPhoneNumber(memberSaveParam.getEmail(), memberSaveParam().getNickname(), memberSaveParam.getPhoneNumber()))
                .thenReturn(Optional.ofNullable(member)); // 이메일이 중복되는 상황 가정.

        // when & then
        MemberException memberException = Assertions.assertThrows(new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS).getClass(), () -> memberSaveService.addMember(memberSaveParam));
        Assertions.assertEquals(MemberErrorCode.NICKNAME_ALREADY_EXISTS, memberException.getErrorCode());

        verify(memberRepository, never()).save(any(Member.class)); // Save 가 한번도 호출되지 않음.
    }

    @Test
    @DisplayName("유저 저장 실패 - 전화번호 중복")
    public void savePhoneNumberDuplicatedUser() {
        // given
        MemberSaveParam memberSaveParam = memberSaveParam();
        Member member = Member.builder()
                .name(memberSaveParam.getName())
                .email(memberSaveParam.getEmail() + "salt")
                .nickname(memberSaveParam.getNickname() + "salt")
                .phoneNumber(memberSaveParam.getPhoneNumber())
                .encryptedPassword(memberSaveParam().getPassword())
                .role(Role.USER)
                .build();
        when(memberRepository.findFirstByEmailOrNicknameOrPhoneNumber(memberSaveParam.getEmail(), memberSaveParam.getNickname(), memberSaveParam().getPhoneNumber()))
                .thenReturn(Optional.ofNullable(member)); // 이메일이 중복되는 상황 가정.

        // when & then
        MemberException memberException = Assertions.assertThrows(new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS).getClass(), () -> memberSaveService.addMember(memberSaveParam));
        Assertions.assertEquals(MemberErrorCode.PHONE_NUMBER_ALREADY_EXISTS, memberException.getErrorCode());

        verify(memberRepository, never()).save(any(Member.class)); // Save 가 한번도 호출되지 않음.
    }
}
