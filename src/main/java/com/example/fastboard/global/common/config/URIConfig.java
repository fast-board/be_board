package com.example.fastboard.global.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;


@Configuration
public class URIConfig {


    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static final List<String> POST_PERMITTED_URIS = Arrays.asList(
            "/api/members/login",
            "/api/members/join",
            "/api/members/reissue"
    );

    public static final List<String> GET_PERMITTED_URIS = Arrays.asList(
            "/api/members/test2",
            "/h2-console/",
            "/favicon.ico",
            "/api/images/**"
    );

    public static final List<String> PERMIT_ALL_URIS = Arrays.asList(
            "/h2-console/**"
    );

    public boolean isNeedAuthentication(String requestURI, String requestMethod) {

        System.out.println(requestURI);
        System.out.println(requestMethod);

        for (String pattern : PERMIT_ALL_URIS) {
            if (antPathMatcher.match(pattern, requestURI)) {
                return false;
            }
        }

        if ("POST".equals(requestMethod)) {
            return !POST_PERMITTED_URIS.contains(requestURI);
        } else if ("GET".equals(requestMethod)) {
            for (String pattern : GET_PERMITTED_URIS) {
                if (antPathMatcher.match(pattern, requestURI)) {
                    return false;
                }
            }
        }

        // 다른 HTTP 메서드에 대해서는 기본적으로 인증이 필요하다고 설정
        return true;
    }
}

