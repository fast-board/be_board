package com.example.fastboard.domain.wish.service;

import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.service.BoardGetService;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberFindService;
import com.example.fastboard.domain.wish.dto.parameter.WishPostParam;
import com.example.fastboard.domain.wish.entity.Wish;
import com.example.fastboard.domain.wish.exception.WishErrorCode;
import com.example.fastboard.domain.wish.exception.WishException;
import com.example.fastboard.domain.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishPostService {
    private final WishRepository wishRepository;
    private final MemberFindService memberFindService;
    private final BoardGetService boardGetService;

    public Wish postWish(WishPostParam wishPostParam) {

        if (wishRepository.existsByUserIdAndBoardId(wishPostParam.userId, wishPostParam.boardId)) {
            throw new WishException(WishErrorCode.WISH_ALEADY_EXISTS);
        }

        Board board = boardGetService.getBoard(wishPostParam.boardId);
        Member member = memberFindService.findMemberById(wishPostParam.userId);


        Wish wish = Wish.builder()
                .board(board)
                .member(member)
                .build();

        return wishRepository.save(wish);
    }
}
