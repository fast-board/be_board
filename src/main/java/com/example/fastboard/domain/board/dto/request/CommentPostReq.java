package com.example.fastboard.domain.board.dto.request;

import lombok.Builder;

@Builder
public record CommentPostReq (
        String content
) { }
