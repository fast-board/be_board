package com.example.fastboard.domain.member.service;

import com.example.fastboard.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    public final MemberService memberService;
    public final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member activeMember = memberService.findActiveMemberByEmail(email);
        return createUserDetails(activeMember);
    }


    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail())
                .password(passwordEncoder.encode(member.getPassword()))
                .roles(String.valueOf(member.getRole()))
                .build();
    }
}
