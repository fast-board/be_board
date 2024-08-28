package com.example.fastboard.domain.member.controller;


import com.example.fastboard.domain.member.dto.TokenDto;
import com.example.fastboard.domain.member.dto.request.MemberLoginRequest;
import com.example.fastboard.domain.member.service.AuthService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<TokenDto>> authenticatedMember(@RequestBody MemberLoginRequest request) {
        TokenDto token = authService.login(request);
        return ResponseEntity.ok()
                .body(ResponseDTO.okWithData(token));
    }
}
