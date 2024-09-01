package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.response.TokenResponse;
import com.example.fastboard.domain.member.dto.request.MemberLoginRequest;
import com.example.fastboard.domain.member.dto.request.RefreshTokenRequest;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Token;
import com.example.fastboard.domain.member.exception.InvalidPasswordException;
import com.example.fastboard.global.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse login(MemberLoginRequest request) {
        Member member = memberService.findActiveMemberByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), member.getEncryptedPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtProvider.createAccessToken(member.getId(),member.getRole().getRoleName());
        String refreshToken = jwtProvider.createRefreshToken();

        refreshTokenService.saveTokenInfo(member.getId(), refreshToken, accessToken);
        return TokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse reissueAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        Member member = refreshTokenService.getMemberFromRefreshToken(refreshToken);
        Token token = refreshTokenService.findTokenByRefreshToken(refreshToken);
        String oldAccessToken = token.getAccessToken();

        // 이전에 발급된 액세스 토큰이 만료가 되어야 새로운 액세스 토큰 발급
        jwtProvider.checkExpiredToken(oldAccessToken);

        String newAccessToken = jwtProvider.createAccessToken(member.getId(),member.getRole().getRoleName());
        token.setAccessToken(newAccessToken);
        refreshTokenService.updateToken(token);
        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
