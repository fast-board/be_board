package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.dto.parameter.BoardUpdateParam;
import com.example.fastboard.domain.board.dto.parameter.CommentDeleteParam;
import com.example.fastboard.domain.board.dto.parameter.CommentPostParam;
import com.example.fastboard.domain.board.dto.request.BoardPostReq;
import com.example.fastboard.domain.board.dto.request.CommentPostReq;
import com.example.fastboard.domain.board.dto.response.BoardGetRes;
import com.example.fastboard.domain.board.dto.response.BoardPageRes;
import com.example.fastboard.domain.board.dto.response.BoardPostRes;
import com.example.fastboard.domain.board.dto.response.CommentGetRes;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.BoardComment;
import com.example.fastboard.domain.board.service.*;
import com.example.fastboard.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardPostService boardPostService;
    private final BoardGetService boardGetService;
    private final BoardDeleteService boardDeleteService;
    private final CommentPostService commentPostService;
    private final CommentGetService commentGetService;
    private final CommentDeleteService commentDeleteService;

    @PostMapping
    public ResponseEntity<ApiResponse> post(@RequestBody BoardPostReq boardPostReq, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Board board = boardPostService.create(boardPostReq.toBoardPostParam(userId));

        BoardPostRes boardPostRes = new BoardPostRes(board.getMember().getName(), board.getId(), board.getTitle(), board.getContent());
        ApiResponse response = new ApiResponse(HttpStatus.CREATED.value(), "게시글이 생성되었습니다.", boardPostRes);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse> getBoard(@PathVariable Long boardId) {
        Board board = boardGetService.getBoard(boardId);
        BoardGetRes boardGetRes = BoardGetRes.builder()
                .id(boardId)
                .author(board.getMember().getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .view(board.getView())
                .authorId(board.getMember().getId())
                .build();

        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), null, boardGetRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String content,
                                              @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                              @RequestParam(required = false, defaultValue = "createdAt", value = "page") String criteria,
                                              Principal principal) {
        List<Board> boards = new ArrayList<>();

        if (title != null) {
            boards = boardGetService.getBoardByTitle(title, pageNo, criteria);
        }

        List<BoardPageRes> boardPostResList = new ArrayList<>();
        for (Board board : boards) {
            BoardPageRes res = BoardPageRes.builder()
                    .id(board.getId())
                    .commentCount(board.getCommentCount())
                    .content(board.getContent())
                    .view(board.getView())
                    .wishCount(board.getWishCount())
                    .title(board.getTitle())
                    .build();

            boardPostResList.add(res);
        }

        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), null, boardPostResList);
        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @GetMapping
    public ResponseEntity<ApiResponse> getBoardList(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                    @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria) {
        List<Board> boards = boardGetService.getBoardList(pageNo, criteria);

        List<BoardPageRes> boardPostResList = new ArrayList<>();
        for (Board board : boards) {
            BoardPageRes res = BoardPageRes.builder()
                    .id(board.getId())
                    .commentCount(board.getCommentCount())
                    .content(board.getContent())
                    .view(board.getView())
                    .wishCount(board.getWishCount())
                    .title(board.getTitle())
                    .build();

            boardPostResList.add(res);
        }

        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), null, boardPostResList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long boardId, @RequestBody BoardPostParam boardPostParam, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        BoardUpdateParam boardUpdateParam = BoardUpdateParam.builder()
                .boardId(boardId)
                .category(boardPostParam.category())
                .content(boardPostParam.content())
                .authorId(userId)
                .title(boardPostParam.title())
                .build();

        Board board = boardPostService.update(boardUpdateParam);

        BoardPostRes boardPostRes = new BoardPostRes(board.getMember().getName(), board.getId(), board.getTitle(), board.getContent());
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "게시글이 수정되었습니다..", boardPostRes);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long boardId, Principal principal) {
        boardDeleteService.deleteBoard(boardId);
        ApiResponse response = new ApiResponse(HttpStatus.NO_CONTENT.value(), "게시글이 삭제되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{boardId}/comments")
    public ResponseEntity<ApiResponse> postComment(@PathVariable Long boardId, @RequestBody CommentPostReq commentPostReq, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        CommentPostParam commentPostParam = new CommentPostParam(boardId, userId, null ,commentPostReq);
        BoardComment boardComment = commentPostService.saveComment(commentPostParam);

        CommentGetRes body = CommentGetRes.builder()
                .commentId(boardComment.getId())
                .author(boardComment.getMember().getNickname())
                .content(boardComment.getContent())
                .authorId(boardComment.getMember().getId())
                .build();

        ApiResponse response = new ApiResponse(HttpStatus.CREATED.value(), "댓글이 생성되었습니다", body);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> postChildComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentPostReq commentPostReq, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        CommentPostParam commentPostParam = new CommentPostParam(boardId, userId, commentId ,commentPostReq);
        BoardComment boardComment = commentPostService.saveComment(commentPostParam);

        CommentGetRes body = CommentGetRes.builder()
                .commentId(boardComment.getId())
                .author(boardComment.getMember().getNickname())
                .content(boardComment.getContent())
                .authorId(boardComment.getMember().getId())
                .build();

        ApiResponse response = new ApiResponse(HttpStatus.CREATED.value(), "대댓글이 생성되었습니다", body);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<ApiResponse> getComments(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                   @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria,
                                                   @PathVariable Long boardId) {
       List<BoardComment> boardComments = commentGetService.getComments(boardId, pageNo, criteria);

       List<CommentGetRes> commentGetResList = new ArrayList<>();
       Map<Long, CommentGetRes> map = new HashMap<>();

       boardComments.stream().forEach(c -> {
           CommentGetRes comment = CommentGetRes.builder()
                   .commentId(c.getId())
                   .content(c.getContent())
                   .author(c.getMember().getNickname())
                   .authorId(c.getMember().getId())
                   .childComments(new ArrayList<>())
                   .build();

           map.put(c.getId(), comment);
           if (c.getParentComment() != null) {
               map.get(c.getParentComment().getId()).childComments().add(comment);
           }
           else commentGetResList.add(comment);
       });


       ApiResponse body = new ApiResponse(HttpStatus.OK.value(), null, commentGetResList);
       return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long boardId, @PathVariable Long commentId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        CommentDeleteParam param = CommentDeleteParam.builder()
                .commentId(commentId)
                .boardId(boardId)
                .userId(userId)
                .build();

        commentDeleteService.deleteComment(param);

        ApiResponse body = new ApiResponse(HttpStatus.NO_CONTENT.value(), "댓글이 삭제되었습니다.", null);
        return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
    }
}
