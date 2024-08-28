package com.example.fastboard.global.common.auth.filter;


import com.example.fastboard.global.common.auth.exception.AuthException;
import com.example.fastboard.global.common.exception.ErrorCode;
import com.example.fastboard.global.common.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            // 예외처리 응답.
            ErrorCode errorCode = e.getErrorCode();
            String message = errorCode.getMessage();
            HttpStatus status = errorCode.getHttpStatus();

            ApiResponse apiResponse = new ApiResponse(status.value(), message, null);
            writeResponseBody(response, apiResponse);
        }
    }


    protected void writeResponseBody(HttpServletResponse response, ApiResponse apiResponse) throws IOException {
        response.setStatus(apiResponse.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(body);
    }
}
