package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    Optional<BoardComment> findById(Long id);
}
