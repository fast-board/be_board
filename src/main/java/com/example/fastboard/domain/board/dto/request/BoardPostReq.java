package com.example.fastboard.domain.board.dto.request;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardPostReq(
        @NotNull(message = "제목을 입력해주세요.")
        @NotBlank(message = "제목을 입력해주세요.")
        String title,

        @NotNull(message = "내용을 입력해주세요.")
        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        Category category
) {
    public BoardPostParam toBoardPostParam(Long authorId) {
        return new BoardPostParam(title, content, category, authorId);
    }
}
