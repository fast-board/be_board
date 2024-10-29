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
        this.name = memberSaveReq.name();
        this.nickname = memberSaveReq.nickname();
        this.phoneNumber = memberSaveReq.phoneNumber();
        this.email = memberSaveReq.email();
        this.password = memberSaveReq.password();
    }
}
