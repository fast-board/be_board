package com.example.fastboard.global.common.config;

import com.example.fastboard.global.common.auth.exception.CustomAccessDeniedHandler;
import com.example.fastboard.global.common.auth.filter.JwtExceptionHandlerFilter;
import com.example.fastboard.global.common.auth.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

    private final JwtFilter jwtFilter;
    private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
    private final URIConfig uriConfig;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // Security 적용 X
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico", "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, uriConfig.POST_PERMITTED_URIS.toArray(new String[0])).permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.GET, uriConfig.GET_PERMITTED_URIS.toArray(new String[0])).permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/members/test").hasRole("ADMIN"))
                .authorizeHttpRequests(request -> request.anyRequest().permitAll()) // 이외에 권한은 로그인이 필요하다.

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionHandlerFilter, jwtFilter.getClass())

                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(customAccessDeniedHandler)
                        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
