package com.example.fastboard.domain.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalName;
    private String saveName;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    private BoardImage(String originalName, String saveName, Board board) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
