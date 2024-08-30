package com.example.fastboard.global.common.auth.service;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.global.common.auth.RefreshToken;
import com.example.fastboard.global.common.auth.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class TokenService {
    private static final long accessTokenExpireTime = 1000 * 60 * 60; // 1시간.
    private static final long refreshTokenExpireTime = 1000 * 60 * 60 * 24; // 24시간.
    private final TokenRepository tokenRepository;
    private final Key key;
    private final Key refreshKey;


    public TokenService(@Value("${jwt.secret}") String secretKey, @Value("${jwt.refreshSecret}") String refreshKey ,TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshKey);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String generateToken(Member member) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + accessTokenExpireTime);

        Claims claims = Jwts.claims();
        claims.put("memberId", member.getId());
        claims.put("role", member.getRole().getRoleName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long memberId) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + refreshTokenExpireTime);

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        tokenRepository.deleteById(memberId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = getRefreshClaims(refreshToken);

            if (claims.getExpiration().before(new Date()))
                return false;

            Long memberId = claims.get("memberId", Long.class);
            RefreshToken dto = tokenRepository.findById(memberId).orElseThrow(() -> new RuntimeException());

            if (memberId == dto.getUserId()) return true;

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long getMemberIdFromRefreshToken(String refreshToken) {
        Claims claims = getRefreshClaims(refreshToken);
        return claims.get("memberId", Long.class);
    }

    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return claims.get("memberId", Long.class);
    }

    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 토큰이 만료된 경우.
        } catch (MalformedJwtException e) { // 토큰 형식이 잘못된 경우.
            return null;
        } catch (SecurityException e) { // SignKey 가 잘못된 경우.
            return null;
        }
    }

    private Claims getRefreshClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 토큰이 만료된 경우.
        } catch (MalformedJwtException e) { // 토큰 형식이 잘못된 경우.
            return null;
        } catch (SecurityException e) { // SignKey 가 잘못된 경우.
            return null;
        }
    }
}
