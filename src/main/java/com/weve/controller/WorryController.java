package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.CreateWorryResponse;
import com.weve.dto.response.GetWorriesResponse;
import com.weve.dto.response.GetWorryResponse;
import com.weve.service.WorryService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/worries")
public class WorryController {

    private final WorryService worryService;

    /**
     * 고민 작성하기
     */
    @PostMapping
    public BasicResponse<CreateWorryResponse> createWorry(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody @Valid CreateWorryRequest request) {

        String username = userDetails.getUsername();
        CreateWorryResponse response = worryService.createWorry(username, request);

        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회(JUNIOR ver)
     */
    @GetMapping("/junior")
    public BasicResponse<?> getWorriesForJunior(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        GetWorriesResponse.juniorVer response = worryService.getWorriesForJunior(username);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회(SENIOR ver)
     */
    @GetMapping("/senior")
    public BasicResponse<?> getWorriesForSenior(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        GetWorriesResponse.seniorVer response = worryService.getWorriesForSenior(username);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/junior")
    public BasicResponse<?> getWorryForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetWorryResponse.juniorVer response = worryService.getWorryForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(SENIOR ver)
     */
    @GetMapping("/{worryId}/senior")
    public BasicResponse<?> getWorryForSenior(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetWorryResponse.seniorVer response = worryService.getWorryForSenior(username, worryId);
        return BasicResponse.onSuccess(response);
    }
}
