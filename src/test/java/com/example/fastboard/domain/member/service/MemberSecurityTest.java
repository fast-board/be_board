package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.global.common.auth.service.TokenService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class MemberSecurityTest {

    private static final Logger log = LoggerFactory.getLogger(MemberSecurityTest.class);
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenService tokenService;

    @Test
    @WithAnonymousUser
    public void get_anonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void get_user() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .name("user")
                .role(Role.USER)
                .email("test@email.com")
                .build();
        String token = tokenService.generateToken(member);

        mockMvc.perform(MockMvcRequestBuilders.get("/").header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void get_user_admin() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .name("user")
                .role(Role.USER)
                .email("test@email.com")
                .build();
        String token = tokenService.generateToken(member);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/test").header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }
}
