package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.exception.MemberErrorCode;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSaveService {

    private final MemberRepository memberRepository;


    public Member addMember(MemberSaveParam memberSaveParam) {

        /**
         * TODO : unique 값을 가진 컬럼 : nickname, phoneNumber, email.
         * TODO : 세 가지를 다 묶어서 검색해야하는 것인가?
         */
        if (memberRepository.existsByEmail(memberSaveParam.getEmail())) throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        if (memberRepository.existsByNickname(memberSaveParam.getNickname())) throw new MemberException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);


        Member member = Member.builder()
                .email(memberSaveParam.getEmail())
                .nickname(memberSaveParam.getNickname())
                .encryptedPassword(memberSaveParam.getPassword())
                .phoneNumber(memberSaveParam.getPhoneNumber())
                .name(memberSaveParam.getName())
                .role(Role.USER) // TODO : User Role
                .build();

        return memberRepository.save(member);

    }
}
