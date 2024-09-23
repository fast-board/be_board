package com.example.fastboard.domain.board.dto.response;

import lombok.Builder;

@Builder
public record BoardImageUploadRes(
        Long id,
        String url
) {}
