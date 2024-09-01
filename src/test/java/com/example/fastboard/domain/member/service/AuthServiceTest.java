package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.request.MemberLoginRequest;
import com.example.fastboard.domain.member.dto.response.TokenResponse;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.domain.member.exception.InvalidPasswordException;
import com.example.fastboard.global.config.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MemberService memberService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @InjectMocks
    private AuthService authService;


    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공(){
        // given
        MemberLoginRequest request = new MemberLoginRequest(
                "test@test.com",
                "12345678A`"
        );

        Member member=Member.builder()
                .encryptedPassword("12345678A`")
                .role(Role.USER)
                .build();
        when(passwordEncoder.matches(request.password(),member.getEncryptedPassword())).thenReturn(true);
        when(memberService.findActiveMemberByEmail(request.email())).thenReturn(member); // 추가
        when(jwtProvider.createAccessToken(member.getId(), member.getRole().getRoleName())).thenReturn("mockAccessToken");
        when(jwtProvider.createRefreshToken()).thenReturn("mockRefreshToken");

        // when
        TokenResponse response = authService.login(request);
        // then
        assertThat(response.accessToken()).isEqualTo("mockAccessToken");
        assertThat(response.refreshToken()).isEqualTo("mockRefreshToken");

    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void 로그인_실패_비밀번호_불일치(){
        //given
        MemberLoginRequest request=new MemberLoginRequest(
                "test@test.com",
                "wrongPassword"
        );

        Member member=Member.builder()
                .name("test1")
                .nickname("testNick")
                .email("test@test.com")
                .encryptedPassword(passwordEncoder.encode("12345678A`"))
                .role(Role.USER)
                .build();

        when(memberService.findActiveMemberByEmail(member.getEmail())).thenReturn(member);
        when(passwordEncoder.matches(request.password(), member.getEncryptedPassword())).thenReturn(false);
        // when, then
        assertThatThrownBy(()-> authService.login(request))
                .isInstanceOf(InvalidPasswordException.class);

    }

}
