package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.parameter.CommentPostParam;
import com.example.fastboard.domain.board.dto.parameter.CommentUpdateParam;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.BoardComment;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardCommentRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentPostService {
    private final BoardCommentRepository boardCommentRepository;
    private final BoardGetService boardGetService;
    private final MemberFindService memberFindService;
    private final CommentGetService commentGetService;

    public BoardComment saveComment(CommentPostParam commentPostParam) {
        // 보드 조회.
        Board board = boardGetService.getBoard(commentPostParam.boardId);

        // 유저 조회.
        Member member = memberFindService.findMemberById(commentPostParam.userId);

        // 부모 코멘트 조회.
        BoardComment parentComment = null;
        if (commentPostParam.parentId != null)
            parentComment = boardCommentRepository.findById(commentPostParam.parentId).orElseThrow(() -> new BoardException(BoardErrorCode.NOT_FOUND_COMMENT));


        BoardComment comment = BoardComment.builder()
                .parentComment(parentComment)
                .member(member)
                .content(commentPostParam.content)
                .parentBoard(board)
                .build();

        return boardCommentRepository.save(comment);
    }

    public BoardComment updateComment(CommentUpdateParam param) {
        BoardComment comment = commentGetService.getComment(param.commentId());

        if (comment.getMember().getId() != param.userId())
            throw new BoardException(BoardErrorCode.NOT_COMMENT_USER);

        comment.setContent(param.content());
        return boardCommentRepository.save(comment);
    }
}
