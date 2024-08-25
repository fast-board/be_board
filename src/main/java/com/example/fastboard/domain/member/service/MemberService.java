package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.request.MemberCreateRequest;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.MemberAlreadyException;
import com.example.fastboard.domain.member.exception.MemberDeletedException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(MemberCreateRequest request) {
        if (isAvailableEmail(request.email())) {
            memberRepository.findByNickname(request.nickname())
                    .ifPresent(mem -> {
                        throw new MemberAlreadyException(ErrorCode.MEMBER_NICKNAME_ALREADY_EXIST_EXCEPTION);
                    });
            memberRepository.findByPhoneNumber(request.phoneNumber())
                    .ifPresent(mem -> {
                        throw new MemberAlreadyException(ErrorCode.MEMBER_PHONE_NUMBER_ALREADY_EXIST_EXCEPTION);
                    });

            String encryptedPassword = request.password(); // 패스워드 암호화 기능 추가 필요
            Member newMember = request.toEntity(encryptedPassword);
            memberRepository.save(newMember);
        }
    }

    private Boolean isAvailableEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            if (member.get().isDelete()) {
                throw new MemberDeletedException();
            } else {
                throw new MemberAlreadyException(ErrorCode.MEMBER_ALREADY_REGISTERED_EXCEPTION);
            }
        }
        return true;
    }
}
