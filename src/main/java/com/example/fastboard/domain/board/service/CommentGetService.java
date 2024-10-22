package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.BoardComment;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentGetService {
    private final BoardCommentRepository boardCommentRepository;

    public List<BoardComment> getComments(Long boardId ,int pageNo, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, criteria));
        return boardCommentRepository.findAllByParentBoardId(boardId, pageable).getContent();
    }

    public BoardComment getComment(Long commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardException(BoardErrorCode.NOT_FOUND_COMMENT));
    }
}
