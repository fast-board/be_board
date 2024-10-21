package com.example.fastboard.domain.wish.repository;

import com.example.fastboard.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {


    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN TRUE ELSE FALSE END FROM Wish w WHERE w.member.id = :userId AND w.board.id = :boardId")
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);

    @Modifying
    @Query("DELETE FROM Wish w WHERE w.member.id = :userId AND w.board.id = :boardId")
    void deleteByUserIdAndBoardId(Long userId, Long boardId);

}
