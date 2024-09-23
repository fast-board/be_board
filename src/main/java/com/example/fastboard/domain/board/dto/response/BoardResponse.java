package com.example.fastboard.domain.board.dto.response;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.Category;
import com.example.fastboard.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record BoardResponse(

        String title,
        String email,
        Long view,
        Long wish,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,
        Category category
) {
    public static BoardResponse fromEntities(Board board, Member member) {
        return new BoardResponse(
                board.getTitle(),
                member.getEmail(),
                board.getView(),
                (long) board.getWishes().size(),
                board.getCreatedAt(),
                board.getCategory()
        );
    }
}
