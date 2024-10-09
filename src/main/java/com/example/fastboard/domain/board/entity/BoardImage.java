package com.example.fastboard.domain.board.entity;

import com.example.fastboard.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE board_image SET board_id = null, deleted_at = now() WHERE id = ?")
public class BoardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalName;
    private String saveName;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public BoardImage(String originalName, String saveName, Board board) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.board = board;
    }
}
