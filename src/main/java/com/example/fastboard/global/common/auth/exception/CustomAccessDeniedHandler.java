package com.example.fastboard.global.common.auth.exception;

import com.example.fastboard.global.common.exception.ErrorCode;
import com.example.fastboard.global.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 권한이 없는 경우 (403) 발생하는 에러를 Handling.
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = AuthErrorCode.IS_NOT_VALID_AUTHORIZE;
        ApiResponse apiResponse = new ApiResponse(errorCode.getHttpStatus().value(), errorCode.getMessage(), null);
        writeResponseBody(response, apiResponse);
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
