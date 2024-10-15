package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.exception.FileException;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberService;
import com.example.fastboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;
    private final MemberService memberService;

    @Value("${file.dir}")
    private String uploadDirectory;
    private final static long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Transactional
    public String store(MultipartFile file, Long memberId) {
        memberService.findActiveMemberById(memberId);

        validateFile(file);
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new FileException(ErrorCode.FILE_ORIGINAL_NAME_IS_EMPTY_EXCEPTION);
        }
        String uniqueFileName = generateUniqueFileName(originalFileName);

        Path filePath = Paths.get(uploadDirectory + File.separator + uniqueFileName);
        createDirectoryIfNotExists(filePath.getParent());
        saveFile(file, filePath);

        BoardImage boardImage = BoardImage.builder()
                .originalName(originalFileName)
                .saveName(uniqueFileName)
                .build();
        boardImageRepository.save(boardImage);

        return filePath.toString();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(ErrorCode.FILE_IS_EMPTY_EXCEPTION);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileException(ErrorCode.FILE_SIZE_LIMIT_EXCEPTION);
        }
    }

    private void createDirectoryIfNotExists(Path directoryPath) {
        try {
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            handleFileSystemException(e, directoryPath, "디렉토리 생성");
        }
    }

    private void saveFile(MultipartFile file, Path filePath) {
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            handleFileSystemException(e, filePath, "파일 저장");
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }

    private void handleFileSystemException(IOException e, Path path, String operation) {
        if (e instanceof NoSuchFileException) {
            log.error("{} 실패: 경로가 존재하지 않음: {}", operation, path, e);
            throw new FileException(ErrorCode.PATH_NOT_FOUND, operation + " 경로가 존재하지 않음");
        } else if (e instanceof FileAlreadyExistsException) {
            log.error("{} 실패: 동일한 파일 또는 디렉토리가 이미 존재: {}", operation, path, e);
            throw new FileException(ErrorCode.FILE_ALREADY_EXISTS, operation + " 동일한 파일/디렉토리 존재");
        } else {
            log.error("{} 실패: 알 수 없는 IO 에러 발생: {}", operation, path, e);
            throw new FileException(ErrorCode.FILE_IOEXCEPTION, operation + " 실패");
        }
    }
}