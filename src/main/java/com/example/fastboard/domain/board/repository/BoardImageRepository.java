package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {
    Optional<BoardImage> findBySaveName(String uniqueFileName);
}
