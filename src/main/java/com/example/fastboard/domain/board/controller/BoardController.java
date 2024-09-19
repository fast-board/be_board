package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.service.BoardService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Long>> create(
            @RequestBody BoardCreateRequest request,
            Principal principal
    ) {
        Long memberId = Long.valueOf(principal.getName());
        Long boardId = boardService.create(request, memberId);

        return ResponseEntity.ok(ResponseDTO.okWithData(boardId));
    }

}
