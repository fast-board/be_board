package com.example.fastboard.global.config.jwt;

import com.example.fastboard.global.common.ResponseDTO;
import com.example.fastboard.global.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {

    public static void sendResponse(HttpServletResponse response, ResponseDTO<?> responseDTO) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(responseDTO.getStatus());

        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.print(objectMapper.writeValueAsString(responseDTO));
        out.flush();
    }
}
