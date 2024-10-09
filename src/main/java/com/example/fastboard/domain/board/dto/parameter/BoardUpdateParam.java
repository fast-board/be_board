package com.example.fastboard.domain.board.dto.parameter;

import com.example.fastboard.domain.board.entity.Category;
import lombok.Builder;

@Builder
public record BoardUpdateParam(
        Long boardId,
        String title,
        String content,
        Long authorId,
        Category category
) {
    static BoardUpdateParam of(Long boardId, String title, String content, Long authorId, Category category) {
        return new BoardUpdateParam(boardId, title, content, authorId, category);
    }
}