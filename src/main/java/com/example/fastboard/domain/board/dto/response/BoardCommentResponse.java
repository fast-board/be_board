package com.example.fastboard.domain.board.dto.response;

import com.example.fastboard.domain.board.entity.BoardComment;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record BoardCommentResponse(
        String email,
        String content,
        List<BoardCommentResponse> childComments,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt
) {

    public static BoardCommentResponse fromEntity(BoardComment boardComment) {
        // 댓글 기능 구현 후 추가 예정
        return null;
    }
}
