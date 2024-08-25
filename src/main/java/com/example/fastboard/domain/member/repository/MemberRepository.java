package com.example.fastboard.domain.member.repository;

import com.example.fastboard.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhoneNumber(String phone);

    Optional<Member> findFirstByEmailOrNicknameOrPhoneNumber(String email, String nickname, String phone);
}
