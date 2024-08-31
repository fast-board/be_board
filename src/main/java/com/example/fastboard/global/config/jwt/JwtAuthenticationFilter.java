package com.example.fastboard.global.config.jwt;

import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHENTICATION_TYPE = "Bearer";
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final int START_TOKEN_INDEX = 6;

    private final JwtProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractToken(request);

        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 유효성 검사 및 인증 객체 저장
        if (StringUtils.hasText(accessToken) && jwtTokenProvider.isValidateToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER_KEY);

        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.toLowerCase().startsWith(AUTHENTICATION_TYPE.toLowerCase())) {
                String token = bearerToken.substring(START_TOKEN_INDEX);
                validateExtractToken(token);
                return token;
            }
            throw new AuthException(ErrorCode.AUTHORIZATION_HEADER_MUST_START_BEARER_EXCEPTION);
        }
        return null;
    }

    private void validateExtractToken(String extractToken) {
        if (extractToken.isBlank()) {
            throw new AuthException(ErrorCode.INVALID_TOKEN_SIGNATURE_EXCEPTION);
        }
    }
}
