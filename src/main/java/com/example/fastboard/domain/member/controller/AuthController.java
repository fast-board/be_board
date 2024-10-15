package com.example.fastboard.domain.member.controller;


import com.example.fastboard.domain.member.dto.response.TokenResponse;
import com.example.fastboard.domain.member.dto.request.MemberLoginRequest;
import com.example.fastboard.domain.member.dto.request.RefreshTokenRequest;
import com.example.fastboard.domain.member.service.AuthService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<TokenResponse>> authenticatedMember(@RequestBody MemberLoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok()
                .body(ResponseDTO.okWithData(token));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDTO<TokenResponse>> reissue(@RequestBody RefreshTokenRequest request) {;
        return ResponseEntity.ok()
                .body(ResponseDTO.okWithData(authService.reissueAccessToken(request)));
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseDTO<Void>> test(Principal id){
        String name = id.getName();
        System.out.println(name);
        return ResponseEntity.ok().body(ResponseDTO.ok());
    }
}

