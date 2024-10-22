package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.parameter.CommentDeleteParam;
import com.example.fastboard.domain.board.entity.BoardComment;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteService {
    private final BoardCommentRepository boardCommentRepository;

    public void deleteComment(CommentDeleteParam param) {
        BoardComment comment = boardCommentRepository.findByIdAndParentBoardId(param.userId(),
                param.commentId(), param.boardId()).orElseThrow(() -> new BoardException(BoardErrorCode.NOT_FOUND_COMMENT));

        boardCommentRepository.delete(comment);
    }
}
