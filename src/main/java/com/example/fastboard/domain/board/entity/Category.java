package com.example.fastboard.domain.board.entity;

import lombok.Getter;

@Getter
public enum Category {
    FIRST("카테고리 1"),
    SECOND("카테고리 2");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
