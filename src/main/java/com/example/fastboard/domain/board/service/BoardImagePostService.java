package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardImagePostService {
    private final BoardImageRepository boardImageRepository;

    @Value("${image.path}")
    private String path;

    public BoardImage upload(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String extension = "";

            if (fileName != null && fileName.contains(".")) {
                extension = fileName.substring(fileName.lastIndexOf("."));
            }

            String imagePath = path + UUID.randomUUID() + extension;
            file.transferTo(new File(imagePath));

            BoardImage boardImage = BoardImage.builder()
                    .originalName(file.getOriginalFilename())
                    .saveName(imagePath)
                    .build();

            return boardImageRepository.save(boardImage);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
