package com.example.fastboard.domain.member.dto.response;

import com.example.fastboard.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberSaveRes {
    private String name;
    private String nickname;
    private String phoneNumber;
    private String email;

    public MemberSaveRes(Member member) {
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
    }
}
