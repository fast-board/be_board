package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardRepository boardRepository;
    private final BoardImageDeleteService boardImageDeleteService;

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
        boardImageDeleteService.deleteByBoardId(boardId);
    }
}
