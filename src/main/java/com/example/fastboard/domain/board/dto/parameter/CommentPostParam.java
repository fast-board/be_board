package com.example.fastboard.domain.board.dto.parameter;

import com.example.fastboard.domain.board.dto.request.CommentPostReq;

public class CommentPostParam {
    public Long boardId;
    public Long userId;
    public Long parentId;
    public String content;

    public CommentPostParam(Long boardId, Long userId, Long parentId ,CommentPostReq commentPostReq) {
        this.boardId = boardId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = commentPostReq.content();
    }
}
