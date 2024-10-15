package com.example.fastboard.domain.wish.controller;

import com.example.fastboard.domain.wish.dto.request.WishRequest;
import com.example.fastboard.domain.wish.service.WishService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishes")
public class WishController {

    private static WishService wishService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> create(
            @RequestBody WishRequest request,
            Principal principal
    ){
        Long memberId = Long.valueOf(principal.getName());
        wishService.create(memberId,request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
