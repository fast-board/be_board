package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.dto.request.BoardUpdateRequest;
import com.example.fastboard.domain.board.dto.response.BoardResponse;
import com.example.fastboard.domain.board.service.BoardService;
import com.example.fastboard.global.common.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Long>> create(
            @Valid @RequestBody BoardCreateRequest request,
            Principal principal
    ) {
        Long memberId = Long.valueOf(principal.getName());
        Long boardId = boardService.save(request, memberId);
        return ResponseEntity.ok(ResponseDTO.okWithData(boardId));
    }


    @GetMapping
    public ResponseEntity<ResponseDTO<List<BoardResponse>>> getAllList() {
        List<BoardResponse> allList = boardService.getAllBoards();
        return ResponseEntity.ok(
                ResponseDTO.okWithData(allList)
        );
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ResponseDTO<Long>> update(
            @Valid @RequestBody BoardUpdateRequest request,
            @PathVariable Long boardId,
            Principal principal
    ) {
        Long memberId = Long.valueOf(principal.getName());
        Long updateBoardId = boardService.update(request, memberId, boardId);
        return ResponseEntity.ok(ResponseDTO.okWithData(updateBoardId));
    }
}
