package com.example.fastboard.domain.board.entity;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.global.common.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
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
}
