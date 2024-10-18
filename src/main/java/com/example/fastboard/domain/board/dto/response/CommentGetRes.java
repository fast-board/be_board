package com.example.fastboard.domain.board.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CommentGetRes(
        Long commentId,
        String content,
        Long authorId,
        String author,
        List<CommentGetRes> childComments
) {}
