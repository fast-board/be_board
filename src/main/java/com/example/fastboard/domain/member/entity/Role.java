package com.example.fastboard.domain.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
}
