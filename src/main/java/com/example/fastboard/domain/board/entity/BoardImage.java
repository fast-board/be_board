package com.example.fastboard.domain.board.entity;

import jakarta.persistence.*;
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
}
