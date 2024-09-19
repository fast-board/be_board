package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.request.MemberCreateRequest;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    public void 회원가입_성공() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "test1",
                "testNick",
                "010-1111-111",
                "test@test.com",
                "12345678A`"
        );

        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(memberRepository.findByNickname(request.nickname())).thenReturn(Optional.empty());
        when(memberRepository.findByPhoneNumber(request.phoneNumber())).thenReturn(Optional.empty());

        // when
        memberService.signup(request);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
        assertDoesNotThrow(() -> memberService.signup(request));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    public void 회원가입_실패_이메일_중복() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "test1",
                "testNick",
                "010-1111-111",
                "test@test.com",
                "12345678A`"
        );

        Member existingMember = new Member();
        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.of(existingMember));

        // when, then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(MemberException.class);
    }
}
