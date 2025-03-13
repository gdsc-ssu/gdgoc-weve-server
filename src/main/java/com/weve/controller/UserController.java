package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.domain.User;
import com.weve.dto.request.PatchMypageRequest;
import com.weve.dto.request.SeniorInfoRequest;
import com.weve.dto.response.MypageResponse;
import com.weve.dto.response.SeniorProfileResponse;
import com.weve.repository.UserRepository;
import com.weve.security.CustomUserDetails;
import com.weve.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api")
@Tag(name = "User", description = "User 관련 API입니다.")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 마이페이지 정보 조회
    @GetMapping("/mypage")
    public BasicResponse<MypageResponse> getMypage(@AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        MypageResponse response = userService.getMypage(username).getResult();
        return BasicResponse.onSuccess(response);
    }

    // 마이페이지 정보 수정
    @PatchMapping("/mypage")
    public BasicResponse<MypageResponse> patchMypage(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PatchMypageRequest request) {
        MypageResponse response = userService.patchMypage(userDetails.getUsername(), request).getResult();
        return BasicResponse.onSuccess(response);
    }

    // 어르신 정보 입력
    @PostMapping("/senior")
    public BasicResponse<?> postSeniorInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SeniorInfoRequest request) {

        String username = userDetails.getUsername();
        userService.postSeniorInfo(username, request);

        return BasicResponse.onSuccess(null);
    }

    // 어르신 정보 확인
    @GetMapping("/senior")
    public BasicResponse<SeniorProfileResponse> getSeniorInfo(@AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        SeniorProfileResponse response = userService.getSeniorInfo(username).getResult();
        return BasicResponse.onSuccess(response);
    }
}
