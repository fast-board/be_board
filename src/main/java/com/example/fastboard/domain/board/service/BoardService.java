package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.dto.response.BoardResponse;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final MemberService memberService;

    public Long create(BoardCreateRequest request, Long memberId) {
        Member member = memberService.findActiveMemberById(memberId);
        Board board = request.toEntity(member);
        Board newBoard = boardRepository.save(board);
        return newBoard.getId();
    }

    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAllByDeletedAtIsNull().stream()
                .map(board -> BoardResponse.fromEntities(board, board.getMember()))
                .collect(Collectors.toList());
    }
}
