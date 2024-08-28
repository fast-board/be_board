package com.example.fastboard.global.config.jwt;

import com.example.fastboard.domain.member.dto.TokenDto;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.MemberAlreadyException;
import com.example.fastboard.domain.member.exception.NoAuthoritiesException;
import com.example.fastboard.global.exception.ApplicationException;
import com.example.fastboard.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;
    private final Long accessTokenValidityMs;
    private final Long refreshTokenValidityMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.token.access-expiration-time}") Long accessTokenValidityS,
            @Value("${jwt.token.refresh-expiration-time}") Long refreshTokenValidityS
    ) {
        this.accessTokenValidityMs = accessTokenValidityS;
        this.refreshTokenValidityMs = refreshTokenValidityS;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // AccessToken, RefreshToken을 생성하는 메소드
    public TokenDto generateToken(Member member) {
        String authorities = String.valueOf(member.getRole());

        // AccessToken 생성
        String accessToken = createAccessToken(member.getId(), authorities);

        // RefreshToken 생성 과정
        String refreshToken = createRefreshToken(member.getId());

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(Long id, String authorities) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + this.accessTokenValidityMs);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long id) {
        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + refreshTokenValidityMs);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // AccessToken의 payload에 저장된 사용자 id,권한을 토대로 Authentication을 만들어 반환하는 메서드
    // 해당 메서드를 바탕으로 SecurityContext에 Authentication객체를 저장
    public Authentication getAuthentication(String accessToken) {
        // 토큰에 payload에 저장된 Claims를 추출
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new NoAuthoritiesException();
        }

        // Claim에서 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Claim에 저장된 사용자 아이디를 통해 UserDetails 객체를 생성
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
            throw new ExpiredJwtException(Jwts.header(), Jwts.claims(), "jwt만료 에러");
        }
    }

    // 토큰 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // Header에서 AccessToken을 추출하는 메소드
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        log.debug("bearertoken : {}", bearerToken);
        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith("Bearer") && bearerToken.length() > 7) {
                int tokenStartIndex = 7;
                return bearerToken.substring(tokenStartIndex);
            }
            log.error("시작이 Bearer이 아님");
            throw new MemberAlreadyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return bearerToken;
    }
}
