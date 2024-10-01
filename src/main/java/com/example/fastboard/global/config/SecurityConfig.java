package com.example.fastboard.global.config;

import com.example.fastboard.global.config.jwt.CustomAccessDeniedHandler;
import com.example.fastboard.global.config.jwt.JwtAuthenticationFilter;
import com.example.fastboard.global.config.jwt.JwtExceptionFilter;
import com.example.fastboard.global.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UrlPermissionChecker urlPermissionChecker;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                request -> !urlPermissionChecker.isNeedAuthentication(request.getRequestURI(), request.getMethod())
                        ).permitAll() // 인증이 필요 없는 URL은 모두 허용
                        .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider,urlPermissionChecker), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)

                .build();
    }
}
