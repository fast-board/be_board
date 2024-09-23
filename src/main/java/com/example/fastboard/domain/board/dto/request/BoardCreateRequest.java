package com.example.fastboard.domain.board.dto.request;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.Category;
import com.example.fastboard.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;

public record BoardCreateRequest(
        @NotNull(message = "제목을 입력해주세요")
        String title,
        @NotNull(message = "내용을 입력해주세요")
        String content,
        @NotNull(message = "카테고리를 선택해 주세요")
        Category category
) {

    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .content(content)
                .view(0L)
                .category(category)
                .member(member)
                .build();
    }
}
