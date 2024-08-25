package com.example.fastboard.domain.member.controller;

import com.example.fastboard.domain.member.dto.request.MemberCreateRequest;
import com.example.fastboard.domain.member.service.MemberService;
import com.example.fastboard.global.common.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<Void>> create(@Valid @RequestBody MemberCreateRequest request) {
        memberService.signup(request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
