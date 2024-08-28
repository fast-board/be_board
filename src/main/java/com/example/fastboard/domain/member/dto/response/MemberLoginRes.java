package com.example.fastboard.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginRes {
    private String access_token;

    public MemberLoginRes(String access_token) {
        this.access_token = access_token;
    }
}
