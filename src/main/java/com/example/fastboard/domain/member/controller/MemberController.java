package com.example.fastboard.domain.member.controller;

import com.example.fastboard.domain.member.dto.parameter.MemberSaveParam;
import com.example.fastboard.domain.member.dto.request.MemberLoginReq;
import com.example.fastboard.domain.member.dto.request.MemberReissueReq;
import com.example.fastboard.domain.member.dto.request.MemberSaveReq;
import com.example.fastboard.domain.member.dto.response.MemberLoginRes;
import com.example.fastboard.domain.member.dto.response.MemberSaveRes;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.service.MemberLoginService;
import com.example.fastboard.domain.member.service.MemberSaveService;
import com.example.fastboard.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberSaveService memberSaveService;
    private final MemberLoginService memberLoginService;


    @PostMapping("/join")
    public ResponseEntity<ApiResponse<MemberSaveRes>> addMember(@RequestBody @Valid MemberSaveReq memberSaveReq) {
        Member member = memberSaveService.addMember(new MemberSaveParam(memberSaveReq));
        MemberSaveRes memberSaveRes = MemberSaveRes.of(member);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "회원가입에 성공하였습니다." ,memberSaveRes),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberLoginRes>> loginMember(@RequestBody @Valid MemberLoginReq memberLoginReq) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "로그인 되었습니다.", memberLoginService.loginMember(memberLoginReq.toMemberLoginParam())));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<MemberLoginRes>> reissueToken(@RequestBody MemberReissueReq refreshToken) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),null, memberLoginService.reissue(refreshToken.refreshToken())));
    }



    @GetMapping("/test")
    public void test() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("UserName = {} ", userDetails.getUsername());
    }
}
