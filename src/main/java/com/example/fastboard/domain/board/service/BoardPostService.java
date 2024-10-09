package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.entity.Board;
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
