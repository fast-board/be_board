package com.example.fastboard.domain.board.entity;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.global.common.entity.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class BoardComment extends BaseEntitySoftDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 300, nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board parentBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardComment parentComment;     //부모 댓글
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardComment> childComments = new ArrayList<>();


    @Builder
    public BoardComment(String content, Board parentBoard, Member member, BoardComment parentComment) {
        this.content = content;
        this.parentBoard = parentBoard;
        this.member = member;
        this.parentComment = parentComment;
    }
}
