package com.example.fastboard.domain.member.dto.response;

import com.example.fastboard.domain.member.entity.Member;

public record MemberSaveRes(
   String name,
   String nickname,
   String phoneNumber,
   String email
) {

    public static MemberSaveRes of(Member member) {
        return new MemberSaveRes(member.getName(), member.getNickname(), member.getPhoneNumber(), member.getEmail());
    }

}
