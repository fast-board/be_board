package com.example.fastboard.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class UrlPermissionChecker {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final Map<String, List<String>> PERMITTED_URIS_MAP = new HashMap<>();

    // 생성자에서 초기화
    public UrlPermissionChecker() {
        PERMITTED_URIS_MAP.put("POST", Arrays.asList(
                "/api/members/login",
                "/api/members/signup",
                "/api/members/reissue"
        ));
        PERMITTED_URIS_MAP.put("GET", Arrays.asList(
                "/api/boards",
                "/api/boards/search",
                "/favicon.ico"
        ));
        PERMITTED_URIS_MAP.put("PUT", Arrays.asList());
        PERMITTED_URIS_MAP.put("DELETE", Arrays.asList());
    }

    public static final List<String> PERMIT_ALL_URIS = Arrays.asList(
            "/api/h2-console/**"
    );

    public boolean isNeedAuthentication(String requestURI, String requestMethod) {

        for (String pattern : PERMIT_ALL_URIS) {
            if (antPathMatcher.match(pattern, requestURI)) {
                return false;
            }
        }

        List<String> permittedUris = PERMITTED_URIS_MAP.get(requestMethod.toUpperCase());

        if (permittedUris != null) {
            return !permittedUris.contains(requestURI);
        }

        return true;
    }
}
