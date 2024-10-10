package com.example.fastboard.domain.board.dto;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.BoardDocument;

public class BoardMapper {
    public static BoardDocument toDocument(Board board) {
        if (board == null) {
            return null;
        }

        return new BoardDocument(
                String.valueOf(board.getId()),
                board.getTitle(),
                board.getContent(),
                board.getMember().getNickname(), // 작성자의 닉네임을 가져옴
                (long) board.getWishes().size(),
                board.getView(), // 조회 수
                board.getCategory().name() // 카테고리 (이름으로 변환)
        );
    }
}