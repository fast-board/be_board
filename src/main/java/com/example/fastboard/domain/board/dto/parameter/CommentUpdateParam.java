package com.example.fastboard.domain.board.dto.parameter;

import lombok.Builder;

@Builder
public record CommentUpdateParam(
        Long userId,
        Long commentId,
        Long boardId,
        String content
) {
}
