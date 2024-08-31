package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Token;
import com.example.fastboard.domain.member.exception.TokenException;
import com.example.fastboard.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private final Long refreshTokenValidityMs;
    private final MemberService memberService;
    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenService(
            @Value("${jwt.token.refresh-expiration-time}") Long refreshTokenValidityMs,
            MemberService memberService,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.refreshTokenValidityMs = refreshTokenValidityMs;
        this.memberService = memberService;
        this.redisTemplate = redisTemplate;
    }

    public void saveTokenInfo(Long memberId, String refreshToken, String accessToken) {
        Token token = Token.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .expiration(refreshTokenValidityMs)
                .build();

        redisTemplate.opsForValue().set(refreshToken, token, refreshTokenValidityMs, TimeUnit.MILLISECONDS);
    }

    public Member getMemberFromRefreshToken(String refreshToken) {
        Token token = findTokenByRefreshToken(refreshToken);
        if (token.getExpiration() > 0) {
            Long memberId = token.getMemberId();
            return memberService.findById(memberId);
        }
        throw new TokenException(ErrorCode.EXPIRED_TOKEN_EXCEPTION);
    }

    public Token findTokenByRefreshToken(String refreshToken) {
        Token token = (Token) redisTemplate.opsForValue().get(refreshToken);
        if (token != null) {
            return token;
        }
        throw new TokenException(ErrorCode.REFRESHTOKEN_NOT_FOUND);
    }

    public void updateToken(Token token) {
        redisTemplate.opsForValue().set(token.getRefreshToken(), token, token.getExpiration(), TimeUnit.MILLISECONDS);
    }
}
