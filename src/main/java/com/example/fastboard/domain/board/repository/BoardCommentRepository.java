package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    Optional<BoardComment> findById(Long id);

    @Query("SELECT C FROM BoardComment C JOIN FETCH C.member  WHERE C.parentBoard.id = :boardId  ORDER BY C.parentComment.id NULLS FIRST, C.createdAt ASC")
    Page<BoardComment> findAllByParentBoardId(@Param("boardId") Long boardId, Pageable pageable);


}
