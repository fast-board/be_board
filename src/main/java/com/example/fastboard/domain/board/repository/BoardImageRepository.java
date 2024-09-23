package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Modifying
    @Query("update BoardImage B set B.board.id = :boardId where B.id in :boardImageIdList")
    void updateBoardImage(@Param("boardImageIdList")List<Long> boardImageIdList, @Param("boardId") Long boardId);
}
