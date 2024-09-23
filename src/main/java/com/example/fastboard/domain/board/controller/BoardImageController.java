package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.response.BoardImageUploadRes;
import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.service.BoardImageGetService;
import com.example.fastboard.domain.board.service.BoardImagePostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@RestController
@RequestMapping("/api/images")
@Slf4j
@RequiredArgsConstructor
public class BoardImageController {


    public final BoardImagePostService boardImagePostService;
    public final BoardImageGetService boardImageGetService;

    @PostMapping
    public ResponseEntity<BoardImageUploadRes> createBoardImage(@RequestParam(value = "image") MultipartFile file) {
        BoardImage boardImage = boardImagePostService.upload(file);

        BoardImageUploadRes body = BoardImageUploadRes.builder()
                .id(boardImage.getId())
                .url("http://localhost:8080/api/images/" + boardImage.getId())
                .build();

        return new ResponseEntity<>(body,HttpStatus.CREATED);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable Long imageId) {
        try {
            File image = boardImageGetService.getImage(imageId);
            return ResponseEntity.ok().header("Content-type",Files.probeContentType(image.toPath())).body(FileCopyUtils.copyToByteArray(image));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
