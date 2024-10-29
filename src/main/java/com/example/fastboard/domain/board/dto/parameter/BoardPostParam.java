package com.example.fastboard.domain.board.dto.parameter;

import com.example.fastboard.domain.board.entity.Category;
import lombok.Builder;

@Builder
public record BoardPostParam(
        String title,
        String content,
        Category category,
        Long authorId
) {
}
