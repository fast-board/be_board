package com.example.fastboard.domain.member.entity;

import com.example.fastboard.global.common.entity.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
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

    @Builder
    public Member(Long id, String name, String nickname, String phoneNumber, String email, String encryptedPassword, Role role) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }

    @Builder
    public Member(String name, String nickname, String phoneNumber, String email, String encryptedPassword, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }
}
