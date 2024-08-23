package com.example.fastboard.domain.member.dto.request;

import com.example.fastboard.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSaveReq {
    /**
     * TODO : DTO Validation 필요.
     */
    private String email;
    private String name;
    private String password;
    private String nickname;
    private String phoneNumber;


}
