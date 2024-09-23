package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardImageGetService {

    private final BoardImageRepository boardImageRepository;

    public File getImage(Long imageId) {
        BoardImage image = boardImageRepository.findById(imageId).orElseThrow(() -> new BoardException(BoardErrorCode.IMAGE_NOT_FOUND));
        return new File(image.getSaveName());
    }
}
