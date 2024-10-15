package com.example.fastboard.global.config.jwt;

import com.example.fastboard.global.common.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResponseDTO<Object> responseDTO = ResponseDTO.errorWithMessageAndData(HttpStatus.FORBIDDEN, e.getMessage(),null);
        ResponseUtil.sendResponse(response,responseDTO);
    }
}
