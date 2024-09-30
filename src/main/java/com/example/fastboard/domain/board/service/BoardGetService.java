package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.ViewRedisRepository;
import com.example.fastboard.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardGetService {
    private final BoardRepository boardRepository;
    private final ViewRedisRepository viewRedisRepository;

    public Board getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        Long viewCount = viewRedisRepository.get(board.getId());

        // 조회수 처리 by Redis.
        if (viewCount == null) {
            viewCount = viewRedisRepository.saveWithIncrement(board);
        }

        else {
            viewCount = viewRedisRepository.updateWithIncrement(board.getId(), viewCount);
        }

        board.updateView(viewCount);
        return board;
    }
}
