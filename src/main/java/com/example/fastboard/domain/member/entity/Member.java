package com.example.fastboard.domain.member.entity;

import com.example.fastboard.global.common.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntitySoftDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(length = 100, unique = true)
    private String nickname;
    @Column(length = 100, unique = true)
    private String phoneNumber;
    @Column(length = 100, unique = true)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(nullable = false)
    private Role role;

    @Builder
    private Member(String name, String nickname, String phoneNumber, String email, String encryptedPassword, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }
}
