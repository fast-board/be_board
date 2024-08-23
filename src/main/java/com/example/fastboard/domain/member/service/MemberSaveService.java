package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
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
         */
        if (memberRepository.existsByEmail(memberSaveParam.getEmail())) throw new RuntimeException(); // TODO : 예외 만들기.


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
