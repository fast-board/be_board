package com.example.fastboard.domain.board.entity;

import lombok.Getter;

@Getter
public enum Category {
    HUMOR("유머게시판"),
    FOOTBALL("축구게시판");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
