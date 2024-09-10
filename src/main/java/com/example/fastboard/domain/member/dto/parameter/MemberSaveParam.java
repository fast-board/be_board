package com.example.fastboard.domain.member.dto.parameter;

import com.example.fastboard.domain.member.dto.request.MemberSaveReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class MemberSaveParam {
    private String name;
    private String nickname;
    private String phoneNumber;
    private String email;
    private String password;

    public MemberSaveParam(MemberSaveReq memberSaveReq) {
        this.name = memberSaveReq.getName();
        this.nickname = memberSaveReq.getNickname();
        this.phoneNumber = memberSaveReq.getPhoneNumber();
        this.email = memberSaveReq.getEmail();
        this.password = memberSaveReq.getPassword();
    }
}
