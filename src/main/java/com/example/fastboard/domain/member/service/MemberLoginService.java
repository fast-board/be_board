package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberLoginParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.MemberErrorCode;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.common.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String loginMember(MemberLoginParam memberLoginParam) {
        Member member = memberRepository.findByEmail(memberLoginParam.getEmail()).orElseThrow(() -> new MemberException(MemberErrorCode.EMAIL_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginParam.getPassword(), member.getEncryptedPassword())) throw new MemberException(MemberErrorCode.PASSWORD_NOT_EQUAL);

        return jwtService.generateToken(member);
    }
}
