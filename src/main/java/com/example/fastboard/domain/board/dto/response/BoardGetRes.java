package com.example.fastboard.domain.board.dto.response;

import lombok.Builder;

@Builder
public record BoardGetRes(
        Long id,
        Long authorId,
        String author,
        String title,
        String content,
        Long view
) {}
