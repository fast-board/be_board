package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.request.MemberLoginRequest;
import com.example.fastboard.domain.member.dto.request.RefreshTokenRequest;
import com.example.fastboard.domain.member.dto.response.TokenResponse;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.entity.Token;
import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.domain.member.exception.InvalidPasswordException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.config.jwt.JwtProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공() {
        // given
        MemberLoginRequest request = new MemberLoginRequest(
                "test@test.com",
                "12345678A`"
        );

        Member member = Member.builder()
                .encryptedPassword("12345678A`")
                .role(Role.USER)
                .build();

        when(passwordEncoder.matches(request.password(), member.getEncryptedPassword())).thenReturn(true);
        when(memberService.findActiveMemberByEmail(request.email())).thenReturn(member); // 추가
        when(jwtProvider.createAccessToken(member.getId(), member.getRole().getRoleName())).thenReturn("mockAccessToken");
        when(jwtProvider.createRefreshToken()).thenReturn("mockRefreshToken");
        doNothing().when(refreshTokenService).saveTokenInfo(member.getId(), "mockRefreshToken", "mockAccessToken");

        // when
        TokenResponse response = authService.login(request);

        // then
        assertThat(response.accessToken()).isEqualTo("mockAccessToken");
        assertThat(response.refreshToken()).isEqualTo("mockRefreshToken");
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void 로그인_실패_비밀번호_불일치() {
        // given
        MemberLoginRequest request = new MemberLoginRequest(
                "test@test.com",
                "wrongPassword"
        );

        Member member = Member.builder()
                .name("test1")
                .nickname("testNick")
                .email("test@test.com")
                .encryptedPassword(passwordEncoder.encode("12345678A`"))
                .role(Role.USER)
                .build();

        when(memberService.findActiveMemberByEmail(member.getEmail())).thenReturn(member);
        when(passwordEncoder.matches(request.password(), member.getEncryptedPassword())).thenReturn(false);
        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidPasswordException.class);

    }

    @Test
    @DisplayName("access토큰 재발급 - 성공")
    public void 엑세스토큰_재생성_성공() {
        // given
        String oldAccessToken = "oldAccessToken";
        String refreshToken = "validRefreshToken";
        String newAccessToken = "newAccessToken";
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        Member member = Member.builder()
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        Token token = Token.builder()
                .memberId(member.getId())
                .accessToken(oldAccessToken)
                .refreshToken(refreshToken)
                .build();

        when(refreshTokenService.getMemberFromRefreshToken(refreshToken)).thenReturn(member);
        when(refreshTokenService.findTokenByRefreshToken(refreshToken)).thenReturn(token);
        doNothing().when(jwtProvider).checkExpiredToken(oldAccessToken);
        when(jwtProvider.createAccessToken(member.getId(), member.getRole().getRoleName())).thenReturn(newAccessToken);
        doNothing().when(refreshTokenService).updateToken(token);

        // when
        TokenResponse tokenResponse = authService.reissueAccessToken(request);

        // then
        assertThat(tokenResponse.accessToken()).isNotNull();

    }


}
