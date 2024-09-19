package com.example.fastboard.domain.board.dto.request;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.Category;
import com.example.fastboard.domain.member.entity.Member;

public record BoardCreateRequest(
    String title,
    String content,
    Category category
) {

    public Board toEntity(Member member){
        return Board.builder()
                .title(title)
                .content(content)
                .view(0L)
                .category(category)
                .member(member)
                .build();
    }
}
