package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT B FROM Board B JOIN FETCH B.member WHERE B.id = :id")
    Optional<Board> findById(Long id);

    @Query("SELECT B FROM Board B JOIN FETCH B.member ")
    Page<Board> findAll(Pageable pageable);

    @Query("UPDATE Board B SET B.view = :view WHERE B.id = :id")
    @Modifying
    void updateViewById(Long id, Long view);

}
