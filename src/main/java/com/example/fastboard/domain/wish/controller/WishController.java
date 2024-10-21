package com.example.fastboard.domain.wish.controller;

import com.example.fastboard.domain.wish.dto.parameter.WishPostParam;
import com.example.fastboard.domain.wish.dto.request.WishPostReq;
import com.example.fastboard.domain.wish.entity.Wish;
import com.example.fastboard.domain.wish.service.WishPostService;
import com.example.fastboard.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
public class WishController {

    private final WishPostService wishPostService;

    @PostMapping
    public ResponseEntity<ApiResponse> postWish(@RequestBody WishPostReq wishPostReq, Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        wishPostService.postWish(WishPostParam.builder()
                        .boardId(wishPostReq.boardId())
                        .userId(userId)
                        .build());

        ApiResponse body = new ApiResponse(HttpStatus.CREATED.value(),"해당 게시글을 좋아합니다." ,null);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{boardId}")
    public void deleteWish(@PathVariable Long boardId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
    }
}
