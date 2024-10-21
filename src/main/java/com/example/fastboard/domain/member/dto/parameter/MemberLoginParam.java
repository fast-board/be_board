package com.example.fastboard.domain.member.dto.parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberLoginParam {
    private String email;
    private String password;
}
