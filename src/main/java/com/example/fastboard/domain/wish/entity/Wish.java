package com.example.fastboard.domain.wish.entity;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
public class Wish extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Wish(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
