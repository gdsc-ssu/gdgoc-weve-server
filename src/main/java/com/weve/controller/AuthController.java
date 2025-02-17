package com.weve.controller;

// 인증 컨트롤러
import com.weve.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String phoneNumber = request.get("phoneNumber");
        String birth = request.get("birth");  // null 가능

        if (phoneNumber == null || phoneNumber.isBlank()) {
            return ResponseEntity.badRequest().body("전화번호를 입력하세요.");
        }

        boolean success = authService.register(name, phoneNumber, birth);
        if(success) {
            return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
        } else {
            return ResponseEntity.badRequest().body("이미 존재하는 전화번호입니다.");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");

        if (phoneNumber == null || phoneNumber.isBlank()) {
            return ResponseEntity.badRequest().body("전화번호를 입력하세요.");
        }

        String token = authService.login(phoneNumber);
        if(token == null) {
            return ResponseEntity.status(404).body("등록되지 않은 전화번호입니다.");
        }

        return ResponseEntity.ok(Map.of("token", token));
    }
}
