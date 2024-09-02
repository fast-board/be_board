package com.example.fastboard.global.config.jwt;

import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;
    private final Long accessTokenValidityMs;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.token.access-expiration-time}") Long accessTokenValidityS
    ) {
        this.accessTokenValidityMs = accessTokenValidityS;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long memberId, String authority) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityMs);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("auth", authority)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(){
        return UUID.randomUUID().toString();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String authority = claims.get("auth", String.class);

        if (authority == null) {
            throw new AuthException(ErrorCode.TOKEN_EXCEPTION);
        }

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(authority));

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.EXPIRED_TOKEN_EXCEPTION);
        }
    }

    public boolean isValidateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다. (구조적 문제)");
            throw new AuthException(ErrorCode.INVALID_TOKEN_SIGNATURE_EXCEPTION);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new AuthException(ErrorCode.EXPIRED_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 형식 입니다.");
            throw new AuthException(ErrorCode.UNSUPPORTED_TOKEN_EXCEPTION);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 Null이거나 비어있습니다.");
            throw new AuthException(ErrorCode.ILLEGAL_TOKEN_EXCEPTION);
        }
    }

    public void checkExpiredToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        }catch (ExpiredJwtException e){
            return;
        }
        throw new AuthException(ErrorCode.NOT_EXPIRED_TOKEN_EXCEPTION);
    }
}
