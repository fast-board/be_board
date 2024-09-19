package com.example.fastboard.domain.board.entity;

import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.wish.entity.Wish;
import com.example.fastboard.global.common.BaseEntitySoftDelete;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntitySoftDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 40000, nullable = false)
    private String content;
    @Column(nullable = false)
    private Long view;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImages = new ArrayList<>();
    @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardComment> boardComments = new ArrayList<>();
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    @Builder
    private Board(
            String title,
            String content,
            Long view, Category category,
            Member member,
            List<BoardImage> boardImages,
            List<BoardComment> boardComments,
            List<Wish> wishes
    ) {
        this.title = title;
        this.content = content;
        this.view = view;
        this.category = category;
        this.member = member;
        this.boardImages = boardImages;
        this.boardComments = boardComments;
        this.wishes = wishes;
    }
}
