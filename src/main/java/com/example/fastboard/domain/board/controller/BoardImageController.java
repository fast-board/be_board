package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.service.BoardImageService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class BoardImageController {

    private final BoardImageService boardImageService;

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> upload(
            MultipartFile file,
            Principal principal
    ) {
        Long memberId = Long.valueOf(principal.getName());
        String imgUrl = boardImageService.store(file, memberId);
        return ResponseEntity.ok().body(ResponseDTO.okWithData(imgUrl));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Long imageId
    ) {
        try {
            File file = boardImageService.getImage(imageId);
            return ResponseEntity.ok().header("Content-type", Files.probeContentType(file.toPath())).body(FileCopyUtils.copyToByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
