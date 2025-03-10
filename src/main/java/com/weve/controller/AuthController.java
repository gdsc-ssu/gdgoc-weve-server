package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.dto.request.UserRequestDto;
import com.weve.service.AuthService;
import jakarta.persistence.Basic;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public BasicResponse<String> register(@RequestBody UserRequestDto request) {

        // 전화번호 필수 체크
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            return BasicResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "전화번호를 입력하세요.", null);
        }

        boolean success = authService.register(
                request.getName(),
                request.getPhoneNumber(),
                request.getBirth(),
                request.getUserType(),
                request.getLanguage()
        );

        if (success) {
            return BasicResponse.onSuccess("회원가입 성공");
        } else {
            return BasicResponse.onFailure(ErrorStatus.INVALID_USER_TYPE.getCode(), "이미 존재하는 전화번호입니다.", null);
        }
    }

    // 로그인
    @PostMapping("/login")
    public BasicResponse<?> login(@RequestBody UserRequestDto request) {

        // 전화번호 및 이름 필수 체크
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            return BasicResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "전화번호를 입력하세요.", null);
        }
        if (request.getName() == null || request.getName().isBlank()) {
            return BasicResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "이름을 입력하세요.", null);
        }

        String token = authService.login(request.getPhoneNumber(), request.getName());

        if (token == null) {
            return BasicResponse.onFailure(ErrorStatus.USER_NOT_FOUND.getCode(), "등록되지 않은 사용자입니다.", null);
        }

        return BasicResponse.onSuccess(token);
    }
}
