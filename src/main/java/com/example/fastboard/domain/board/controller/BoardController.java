package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.dto.request.BoardUpdateRequest;
import com.example.fastboard.domain.board.dto.response.BoardDetailResponse;
import com.example.fastboard.domain.board.dto.response.BoardResponse;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.service.BoardService;
import com.example.fastboard.global.common.PageResponseDTO;
import com.example.fastboard.global.common.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<BoardResponse>>> searchList(
            @RequestParam(value = "title", required = false) String title
    ) {
        return ResponseEntity.ok(
                ResponseDTO.okWithData(boardService.searchList(title))
        );
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<BoardResponse>> getAllList(
            @PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        Page<BoardResponse> boardPage = boardService.getAllBoards(adjustedPageable);
        return ResponseEntity.ok(
                PageResponseDTO.okWithData(boardPage)
        );
    }

    @GetMapping("{boardId}")
    public ResponseEntity<ResponseDTO<BoardDetailResponse>> getDetail(
            @PathVariable Long boardId,
            Principal principal
    ) {
        Long memberId = Long.valueOf(principal.getName());
        return ResponseEntity.ok(
                ResponseDTO.okWithData(boardService.loadBoardDetail(boardId, memberId))
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

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDTO<Void>> delete(
            @PathVariable Long boardId,
            Principal principal
    ) {
        Long memberId = Long.valueOf(principal.getName());
        boardService.delete(boardId, memberId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<ResponseDTO<Board>> test(
            @PathVariable Long id
    ) {
        Board test = boardService.test(id);
        return ResponseEntity.ok(
                ResponseDTO.okWithData(test)
        );
    }
}
