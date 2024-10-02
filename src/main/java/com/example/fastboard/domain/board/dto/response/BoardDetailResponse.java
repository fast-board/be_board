package com.example.fastboard.domain.board.dto.response;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.Category;
import com.example.fastboard.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BoardDetailResponse(

        String title,
        String email,
        String content,
        Long view,
        Long wish,
        List<BoardCommentResponse> boardComments,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,
        Category category
) {
    public static BoardDetailResponse fromEntities(Board board) {
        /**
         * 댓글 작성 기능 구현 후 추가 개발 예정.
         */
        List<BoardCommentResponse> commentResponses = board.getBoardComments().stream()
                .map(BoardCommentResponse::fromEntity)
                .collect(Collectors.toList());

        return new BoardDetailResponse(
                board.getTitle(),
                board.getMember().getEmail(),
                board.getContent(),
                board.getView(),
                (long) board.getWishes().size(),
                commentResponses,
                board.getCreatedAt(),
                board.getCategory()
        );
    }
}
