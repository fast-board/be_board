package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.ViewRedisRepository;
import com.example.fastboard.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardGetService {
    private final BoardRepository boardRepository;
    private final ViewRedisRepository viewRedisRepository;

    public Board getBoard(Long boardId) {
        Board board = getBoardNotViewCount(boardId);
        // 조회수 처리 by Redis.
        Long viewCount = viewRedisRepository.get(board);

        board.updateView(viewCount);
        return board;
    }

    public List<Board> getBoardList(int pageNo, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, criteria));
        return boardRepository.findAll(pageable).getContent();
    }

    // 단순 조회.
    public Board getBoardNotViewCount(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    // 제목 검색
    public List<Board> getBoardByTitle(String title, int pageNo, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, criteria));
        return boardRepository.searchByTitle(pageable, title).getContent();
    }
}
