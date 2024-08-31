package com.example.fastboard.global.config.jwt;

import com.example.fastboard.global.common.ResponseDTO;
import com.example.fastboard.global.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {

    public static void sendResponse(HttpServletResponse response, ApplicationException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(e.getErrorCode().getHttpStatus().value());

        ResponseDTO<Object> responseDTO = ResponseDTO.errorWithMessageAndData(e.getErrorCode().getHttpStatus(), e.getMessage(), e.getData());

        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.print(objectMapper.writeValueAsString(responseDTO));
        out.flush();
    }
}
