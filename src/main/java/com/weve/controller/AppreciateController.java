package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.request.PostAppreciateRequest;
import com.weve.dto.response.GetAppreciateResponse;
import com.weve.service.AppreciateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/appreciate")
@Tag(name = "Appreciate", description = "Appreciate 관련 API입니다.")
public class AppreciateController {

    private final AppreciateService appreciateService;

    // 감사인사 작성하기
    @PostMapping
    public BasicResponse<?> postAppreciate(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostAppreciateRequest request) {

        String username = userDetails.getUsername();
        appreciateService.postAppreciate(username, request);
        return BasicResponse.onSuccess(null);
    }

}
