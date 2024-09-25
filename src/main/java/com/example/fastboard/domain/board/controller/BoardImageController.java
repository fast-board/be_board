package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.dto.response.BoardImageUploadRes;
import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.service.BoardImageGetService;
import com.example.fastboard.domain.board.service.BoardImagePostService;
import com.example.fastboard.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse> createBoardImage(@RequestParam(value = "image") MultipartFile file) {
        BoardImage boardImage = boardImagePostService.upload(file);

        BoardImageUploadRes body = BoardImageUploadRes.builder()
                .id(boardImage.getId())
                .url("http://localhost:8080/api/images/" + boardImage.getId())
                .build();

        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(),"이미지를 생성하였습니다." , body);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
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
