package com.example.fastboard.domain.member.entity;

import com.example.fastboard.global.common.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntitySoftDelete implements UserDetails {
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
    private Member(String name, String nickname, String phoneNumber, String email, String encryptedPassword, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
