package com.example.fastboard.domain.wish.dto.parameter;

import lombok.Builder;

public class WishPostParam {
    public Long boardId;
    public Long userId;

    @Builder
    public WishPostParam(Long boardId, Long userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}
