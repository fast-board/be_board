package com.example.fastboard.global.common.config;

import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;


@Configuration
public class URIConfig {

    public static final List<String> POST_PERMITTED_URIS = Arrays.asList(
            "/api/members/login",
            "/api/members/join"
    );

    public static final List<String> GET_PERMITTED_URIS = Arrays.asList(
            "/api/members/test2"
    );

    public boolean isNeedAuthentication(String requestURI, String requestMethod) {

        System.out.println(requestURI);
        System.out.println(requestMethod);

        if ("POST".equals(requestMethod)) {
            return !POST_PERMITTED_URIS.contains(requestURI);
        } else if ("GET".equals(requestMethod)) {
            return !GET_PERMITTED_URIS.contains(requestURI);
        }

        // 다른 HTTP 메서드에 대해서는 기본적으로 인증이 필요하다고 설정
        return true;
    }
}

