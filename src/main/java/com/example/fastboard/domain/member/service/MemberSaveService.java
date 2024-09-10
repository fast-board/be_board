package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberLoginParam;
import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.exception.MemberErrorCode;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.common.auth.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSaveService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberDetailsService customUserDetailsService;


    public Member addMember(MemberSaveParam memberSaveParam) {

        /**
         * TODO : unique 값을 가진 컬럼 : nickname, phoneNumber, email.
         * TODO : 세 가지를 다 묶어서 검색해야하는 것인가?
         */

        memberRepository.findFirstByEmailOrNicknameOrPhoneNumber(
                memberSaveParam.getEmail(),
                memberSaveParam.getNickname(),
                memberSaveParam.getPhoneNumber()
        ).ifPresent(member -> {
            if (member.getEmail().equals(memberSaveParam.getEmail())) throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
            if (member.getNickname().equals(memberSaveParam.getNickname())) throw new MemberException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
            if (member.getPhoneNumber().equals(memberSaveParam.getPhoneNumber())) throw new MemberException(MemberErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
        });

        Member member = Member.builder()
                .email(memberSaveParam.getEmail())
                .nickname(memberSaveParam.getNickname())
                .encryptedPassword(passwordEncoder.encode(memberSaveParam.getPassword()))
                .phoneNumber(memberSaveParam.getPhoneNumber())
                .name(memberSaveParam.getName())
                .role(Role.USER) // TODO : User Role
                .build();

        return memberRepository.save(member);
    }

    public String loginMember(MemberLoginParam memberLoginParam) {
        return null;
    }
}
