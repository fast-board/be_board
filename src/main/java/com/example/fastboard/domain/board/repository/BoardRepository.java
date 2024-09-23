package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByDeletedAtIsNull();
}
