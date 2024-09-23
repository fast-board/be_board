package com.example.fastboard.domain.board.controller;

import com.example.fastboard.domain.board.service.BoardImageService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
