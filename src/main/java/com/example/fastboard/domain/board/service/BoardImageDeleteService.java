package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardImageDeleteService {
    private final BoardImageRepository boardImageRepository;


    @Transactional
    public void deleteByBoardId(Long boardId) {
        boardImageRepository.deleteBoardImageByBoardId(boardId);
    }

    /**
     * Schedule
     */
    /**
     * Schedule
     */
//    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    @Scheduled(fixedDelay = 5000)
    @Async("imageDeleter")
    @Modifying
    @Transactional
    public void deleteImageNotConnectedBoard() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        List<BoardImage> boardImages = boardImageRepository.findBoardImageByBoardIsNullAndUpdatedAtBefore(cutoffTime);
        List<Long> ids = new ArrayList<>();

        // File Delete.
        for (BoardImage boardImage : boardImages) {
            Path path = Path.of(boardImage.getSaveName());
            File file = path.toFile();

            // 파일이 성공적으로 삭제되었는지 확인
            if (file.exists()) {
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    ids.add(boardImage.getId());
                } else {
                    // 파일 삭제 실패 시 예외 로그 기록
                    System.err.println("Failed to delete file: " + boardImage.getSaveName());
                }
            } else {
                System.err.println("File not found: " + boardImage.getSaveName());
            }
        }

        // 파일 삭제가 성공한 이미지들만 RDB 삭제
        if (!ids.isEmpty()) {
            boardImageRepository.deleteBoardImageByIdList(ids);
        }
    }

}
