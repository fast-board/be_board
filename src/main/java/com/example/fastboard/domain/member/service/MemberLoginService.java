package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberLoginParam;
import com.example.fastboard.domain.member.dto.response.MemberLoginRes;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.MemberErrorCode;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.common.auth.RefreshToken;
import com.example.fastboard.global.common.auth.TokenRepository;
import com.example.fastboard.global.common.auth.exception.AuthErrorCode;
import com.example.fastboard.global.common.auth.exception.AuthException;
import com.example.fastboard.global.common.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    public MemberLoginRes loginMember(MemberLoginParam memberLoginParam) {
        Member member = memberRepository.findByEmail(memberLoginParam.getEmail()).orElseThrow(() -> new MemberException(MemberErrorCode.EMAIL_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginParam.getPassword(), member.getEncryptedPassword())) throw new MemberException(MemberErrorCode.PASSWORD_NOT_EQUAL);

        String accessToken = tokenService.generateToken(member);
        String refreshToken = generateRefreshToken(member.getId());

        return new MemberLoginRes(accessToken, refreshToken);
    }

    public MemberLoginRes reissue(String refreshToken) {
        if(!tokenService.validateRefreshToken(refreshToken)) throw new AuthException(AuthErrorCode.IS_NOT_VALID_TOKEN);

        Long memberId = tokenService.getMemberIdFromRefreshToken(refreshToken);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        String accessToken = tokenService.generateToken(member);
        String reIssuedRefreshToken = generateRefreshToken(member.getId());

        return new MemberLoginRes(accessToken, reIssuedRefreshToken);
    }



    protected String generateRefreshToken(Long userId) {
        String refreshToken = tokenService.generateRefreshToken(userId);
        RefreshToken refreshTokenEntity = new RefreshToken(refreshToken, userId);
        tokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }
}
