package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Modifying
    @Query("UPDATE BoardImage B SET B.board.id = :boardId WHERE B.id IN :boardImageIdList")
    void updateBoardImage(@Param("boardImageIdList")List<Long> boardImageIdList, @Param("boardId") Long boardId);

    @Modifying
    void deleteBoardImageByBoardId(Long boardId);

    List<BoardImage> findBoardImageByBoardIsNullAndUpdatedAtBefore(LocalDateTime updatedAt);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM BOARD_IMAGE WHERE id IN :boardImageIdList")
    void deleteBoardImageByIdList(List<Long> boardImageIdList);
}
