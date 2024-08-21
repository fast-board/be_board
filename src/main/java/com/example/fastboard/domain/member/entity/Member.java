package com.example.fastboard.domain.member.entity;

import com.example.fastboard.global.common.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
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
}
