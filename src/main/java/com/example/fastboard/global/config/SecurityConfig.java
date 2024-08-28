package com.example.fastboard.global.config;

import com.example.fastboard.global.common.ResponseDTO;
import com.example.fastboard.global.config.jwt.JwtAuthenticationFilter;
import com.example.fastboard.global.config.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] ALLOWED_PATHS = {
            "/h2-console/**",
            "/boards",
            "/boards/search",
            "/members/signup",
            "/members/login"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        Arrays.stream(ALLOWED_PATHS)
                                                .map(AntPathRequestMatcher::new)
                                                .toArray(RequestMatcher[]::new)
                                ).permitAll()
                                .anyRequest().authenticated()
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {
                                    ResponseDTO<Void> responseDTO = ResponseDTO.errorWithMessage(HttpStatus.FORBIDDEN, "Access Denied");
                                    sendResponse(response, responseDTO);
                                }
                        )
                        .authenticationEntryPoint(
                                (request, response, accessDeniedException) -> {
                                    ResponseDTO<Void> responseDTO = ResponseDTO.errorWithMessage(HttpStatus.UNAUTHORIZED, "Unauthorized: 1. 비로그인 상태로 로그인이 필요한 API에 접근했거나 2. 없는 API URI에 요청을 보내고 있습니다");
                                    sendResponse(response, responseDTO);
                                }
                        )
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    private void sendResponse(HttpServletResponse response, ResponseDTO<Void> responseDTO) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(responseDTO.getStatus());
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.print(objectMapper.writeValueAsString(responseDTO));
        out.flush();
    }
}
