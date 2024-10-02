package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByDeletedAtIsNull();

    @Query("SELECT b FROM Board b JOIN FETCH b.member LEFT JOIN FETCH b.wishes WHERE b.title LIKE %:title% ORDER BY b.createdAt DESC")
    List<Board> findByTitleContainingOrderByCreatedAtDesc(String title);
}
