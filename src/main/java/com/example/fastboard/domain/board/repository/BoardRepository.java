package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select B FROM Board B JOIN FETCH B.member WHERE B.id = :id")
    Optional<Board> findById(Long id);
}
