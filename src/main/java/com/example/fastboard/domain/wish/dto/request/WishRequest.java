package com.example.fastboard.domain.wish.dto.request;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.wish.entity.Wish;

public record WishRequest(
        Long boardId
) {

    public Wish toEntity(Board board, Member member){
        return Wish.builder()
                .board(board)
                .member(member)
                .build();
    }
}
