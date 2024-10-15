package com.example.fastboard.global.config.jwt;

import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.global.common.ResponseDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.errorWithMessageAndData(e.getErrorCode().getHttpStatus(), e.getMessage(), e.getData());
            ResponseUtil.sendResponse(response, responseDTO);
        }
    }
}
