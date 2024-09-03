package com.example.fastboard.domain.member.entity;

import com.example.fastboard.global.common.entity.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private Role role;

}
