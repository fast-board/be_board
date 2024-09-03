package com.example.fastboard.domain.member.controller;

import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.dto.request.MemberSaveReq;
import com.example.fastboard.domain.member.dto.response.MemberSaveRes;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberSaveService;
import com.example.fastboard.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService memberSaveService;


    @PostMapping("/join")
    public ResponseEntity<ApiResponse<MemberSaveRes>> addMember(@RequestBody @Valid MemberSaveReq memberSaveReq) {
        Member member = memberSaveService.addMember(new MemberSaveParam(memberSaveReq));
        MemberSaveRes memberSaveRes = new MemberSaveRes(member);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "회원가입에 성공하였습니다." ,memberSaveRes),HttpStatus.CREATED);
    }
}
