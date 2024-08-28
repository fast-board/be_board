package com.example.fastboard.global.common.auth.filter;

import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.global.common.auth.exception.AuthErrorCode;
import com.example.fastboard.global.common.auth.exception.AuthException;
import com.example.fastboard.global.common.auth.service.JwtService;
import com.example.fastboard.global.common.auth.service.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;
    private final JwtService jwtService;

    // 토큰의 유효성을 검사하지 않아도 되는 URL.
    private final static List<String> PERMITTED_URIS = Arrays.asList(
            "/api/members/login",
            "/api/members/join"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (PERMITTED_URIS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        if (!jwtService.validateToken(token)) { // 토큰이 유효하지 않은 경우.
            throw new AuthException(AuthErrorCode.IS_NOT_VALID_TOKEN);
        }

        Long userId = jwtService.getMemberId(token);
        String role = jwtService.getRole(token);

        UserDetails userDetails = memberDetailsService.loadUserByUsernameWithRole(userId, role);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        filterChain.doFilter(request, response);
    }

    protected String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        else
            return null;
    }
}
