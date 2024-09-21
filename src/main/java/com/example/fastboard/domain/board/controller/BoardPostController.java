package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.request.BoardPostReq;
import com.example.fastboard.domain.board.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardPostController {

    private final BoardPostService boardPostService;

    @PostMapping
    public void post(@RequestBody BoardPostReq boardPostReq, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        boardPostService.create(boardPostReq.toBoardPostParam(userId));
    }
}
