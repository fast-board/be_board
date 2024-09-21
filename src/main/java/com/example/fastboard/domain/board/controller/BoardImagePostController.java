package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.service.BoardImagePostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/images")
@Slf4j
@RequiredArgsConstructor
public class BoardImagePostController {


    public final BoardImagePostService boardImagePostService;

    @PostMapping
    public void createBoardImage(@RequestParam MultipartFile file) {
        BoardImage boardImage = boardImagePostService.upload(file);
    }
}
