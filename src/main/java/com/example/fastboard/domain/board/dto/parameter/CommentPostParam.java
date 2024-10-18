package com.example.fastboard.domain.board.dto.parameter;

import com.example.fastboard.domain.board.dto.request.CommentPostReq;

public class CommentPostParam {
    public Long boardId;
    public Long userId;
    public Long parentId;
    public String content;

    public CommentPostParam(Long boardId, Long userId, CommentPostReq commentPostReq) {
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = commentPostReq.parentCommentId();
        this.content = commentPostReq.content();
    }
}
