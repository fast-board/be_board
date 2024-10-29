package com.example.fastboard.global.common.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findById(Long id);
}
