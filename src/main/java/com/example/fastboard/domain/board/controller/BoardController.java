package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.dto.parameter.BoardUpdateParam;
import com.example.fastboard.domain.board.dto.request.BoardPostReq;
import com.example.fastboard.domain.board.dto.response.BoardGetRes;
import com.example.fastboard.domain.board.dto.response.BoardPageRes;
import com.example.fastboard.domain.board.dto.response.BoardPostRes;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.service.BoardDeleteService;
import com.example.fastboard.domain.board.service.BoardGetService;
import com.example.fastboard.domain.board.service.BoardPostService;
import com.example.fastboard.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardPostService boardPostService;
    private final BoardGetService boardGetService;
    private final BoardDeleteService boardDeleteService;

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
}
