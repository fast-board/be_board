package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.dto.parameter.BoardUpdateParam;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.exception.BoardErrorCode;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberFindService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    private final BoardRepository boardRepository;
    private final BoardImagePostService boardImagePostService;
    private final MemberFindService memberFindService;
    private final BoardImageDeleteService boardImageDeleteService;

    @Transactional
    public Board create(BoardPostParam boardPostParam) {
        Member member = memberFindService.findMemberById(boardPostParam.authorId());
        List<Long> images = getImageId(boardPostParam.content());

        Board board = boardRepository.save(Board.builder()
                .title(boardPostParam.title())
                .content(boardPostParam.content())
                .member(member)
                .category(boardPostParam.category())
                .build());

        if (images.size() > 0) boardImagePostService.connectToBoard(images, board.getId());
        return board;
    }

    @Transactional
    public Board update(BoardUpdateParam boardUpdateParam) {
        Member member = memberFindService.findMemberById(boardUpdateParam.authorId());
        Board board = boardRepository.findById(boardUpdateParam.boardId()).orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        if (member.getId() != board.getMember().getId()) throw new BoardException(BoardErrorCode.MEMBER_NOT_EQUAL);
        board.setTitle(boardUpdateParam.title());
        board.setContent(boardUpdateParam.content());
        board.setCategory(boardUpdateParam.category());

        List<Long> images = getImageId(boardUpdateParam.content());
        boardImageDeleteService.deleteByBoardId(board.getId());
        boardImagePostService.connectToBoard(images, board.getId());

        return boardRepository.save(board);
    }

    private List<Long> getImageId(String body) {
        List<Long> imageIds = new ArrayList<>();
        String regex = "<img\\s+[^>]*src=\"([^\"]+)\"[^>]*>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            imageIds.add(Long.parseLong(imageUrl.substring(imageUrl.lastIndexOf("/") + 1)));
        }

        return imageIds;
    }
}
