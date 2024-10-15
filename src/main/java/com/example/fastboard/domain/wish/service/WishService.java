package com.example.fastboard.domain.wish.service;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.service.BoardService;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberService;
import com.example.fastboard.domain.wish.dto.request.WishRequest;
import com.example.fastboard.domain.wish.entity.Wish;
import com.example.fastboard.domain.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    public void create(Long memberId, WishRequest request) {
        Member member = memberService.findActiveMemberById(memberId);
        Board board = boardService.findActiveBoardById(request.boardId());
        Wish wish = request.toEntity(board, member);
        wishRepository.save(wish);
    }
}
