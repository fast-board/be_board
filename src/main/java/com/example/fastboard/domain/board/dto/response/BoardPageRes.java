package com.example.fastboard.domain.board.dto.response;

import lombok.Builder;

@Builder
public record BoardPageRes(
        Long id,
        String title,
        String content,
        Long view,
        int commentCount,
        int wishCount
) {
}
