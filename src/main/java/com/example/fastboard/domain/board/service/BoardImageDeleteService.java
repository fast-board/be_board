package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.repository.BoardImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardImageDeleteService {
    private final BoardImageRepository boardImageRepository;


    @Transactional
    public void deleteByBoardId(Long boardId) {
        boardImageRepository.deleteBoardImageByBoardId(boardId);
    }
}
