package com.example.fastboard.domain.board.dto.parameter;

import lombok.Builder;

@Builder
public record CommentDeleteParam(
        Long userId,
        Long boardId,
        Long commentId
) {}
