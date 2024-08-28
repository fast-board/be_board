package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.TokenDto;
import com.example.fastboard.domain.member.dto.request.MemberLoginRequest;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.InvalidPasswordException;
import com.example.fastboard.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public TokenDto login(MemberLoginRequest request) {
        Member member = memberService.findActiveMemberByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), member.getEncryptedPassword())) {
            throw new InvalidPasswordException();
        }

        return jwtTokenProvider.generateToken(member);
    }
}
