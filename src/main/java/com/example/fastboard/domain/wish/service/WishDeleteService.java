package com.example.fastboard.domain.wish.service;

import com.example.fastboard.domain.wish.entity.Wish;
import com.example.fastboard.domain.wish.exception.WishErrorCode;
import com.example.fastboard.domain.wish.exception.WishException;
import com.example.fastboard.domain.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishDeleteService {
    private final WishRepository wishRepository;

    public void delete(Long userId, Long boardId) {
        if (!wishRepository.existsByUserIdAndBoardId(userId, boardId)) {
            throw new WishException(WishErrorCode.WISH_NOT_FOUND);
        }

        wishRepository.deleteByUserIdAndBoardId(userId, boardId);
    }
}
