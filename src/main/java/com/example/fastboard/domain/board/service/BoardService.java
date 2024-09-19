package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public Long create(BoardCreateRequest request, Long memberId) {
        Member member = memberService.findActiveMemberById(memberId);
        Board board = request.toEntity(member);
        Board newBoard = boardRepository.save(board);
        return newBoard.getId();
    }
}
