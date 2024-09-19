package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.dto.request.MemberCreateRequest;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.MemberException;
import com.example.fastboard.domain.member.repository.MemberRepository;
import com.example.fastboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberCreateRequest request) {
        if (isAvailableEmail(request.email())) {
            memberRepository.findByNickname(request.nickname())
                    .ifPresent(mem -> {
                        throw new MemberException(ErrorCode.MEMBER_NICKNAME_ALREADY_EXIST_EXCEPTION);
                    });
            memberRepository.findByPhoneNumber(request.phoneNumber())
                    .ifPresent(mem -> {
                        throw new MemberException(ErrorCode.MEMBER_PHONE_NUMBER_ALREADY_EXIST_EXCEPTION);
                    });

            String encryptedPassword = passwordEncoder.encode(request.password()); // 패스워드 암호화 기능 추가 필요
            Member newMember = request.toEntity(encryptedPassword);
            memberRepository.save(newMember);
        }
    }

    private Boolean isAvailableEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            if (member.get().isDelete()) {
                throw new MemberException(ErrorCode.MEMBER_DELETED_EXCEPTION);
            } else {
                throw new MemberException(ErrorCode.MEMBER_ALREADY_REGISTERED_EXCEPTION);
            }
        }
        return true;
    }

    public Member findActiveMemberById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(()-> new MemberException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION));
        if(member.isDelete()){
            throw new MemberException(ErrorCode.MEMBER_DELETED_EXCEPTION);
        }
        return member;
    }

    public Member findActiveMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION));
        if (member.isDelete()) {
            throw new MemberException(ErrorCode.MEMBER_DELETED_EXCEPTION);
        }
        return member;
    }

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(()->new MemberException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION));
    }
}
