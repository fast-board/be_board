package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.request.BoardPostReq;
import com.example.fastboard.domain.board.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardPostController {

    private final BoardPostService boardPostService;

    @PostMapping
    public void post(@RequestBody BoardPostReq boardPostReq) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        boardPostService.create(boardPostReq.toBoardPostParam(userId));
    }
}
