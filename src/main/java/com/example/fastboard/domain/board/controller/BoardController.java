package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.request.BoardPostReq;
import com.example.fastboard.domain.board.dto.response.BoardGetRes;
import com.example.fastboard.domain.board.dto.response.BoardPostRes;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.service.BoardGetService;
import com.example.fastboard.domain.board.service.BoardPostService;
import com.example.fastboard.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardPostService boardPostService;
    private final BoardGetService boardGetService;

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
}
