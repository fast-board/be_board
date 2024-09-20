package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    private final BoardRepository boardRepository;
    private final MemberFindService memberFindService;

    public Board create(BoardPostParam boardPostParam) {

        Member member = memberFindService.findMemberById(boardPostParam.authorId());

        Board board = Board.builder()
                .title(boardPostParam.title())
                .content(boardPostParam.content())
                .member(member)
                .category(boardPostParam.category())
                .build();

        return boardRepository.save(board);
    }
}
