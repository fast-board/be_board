package com.example.fastboard.global.common.auth;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "RefreshToken", timeToLive = 60 * 60 * 24)
public class RefreshToken {

    @Id
    private Long userId;
    private String refreshToken;

    public RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
