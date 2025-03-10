package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.domain.User;
import com.weve.repository.UserRepository;
import com.weve.security.CustomUserDetails;
import com.weve.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/mypage")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 마이페이지 정보 조회
    @GetMapping
    public ResponseEntity<BasicResponse<?>> getMypage(@AuthenticationPrincipal UserDetails userDetails) {
        BasicResponse<?> response = userService.getMypage(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
