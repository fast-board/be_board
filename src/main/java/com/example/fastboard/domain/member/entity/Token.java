package com.example.fastboard.domain.member.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    private Long memberId;
    private String refreshToken;
    private String accessToken;
    private Long expiration;

    public void setAccessToken(String newAccessToken) {
        this.accessToken = newAccessToken;
    }
}
