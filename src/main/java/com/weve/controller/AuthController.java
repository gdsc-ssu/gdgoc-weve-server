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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String phoneNumber = request.get("phoneNumber");

        if (name == null || phoneNumber == null) {
            return ResponseEntity.badRequest().body("이름과 전화번호를 입력하세요.");
        }

        String token = authService.login(name, phoneNumber);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
