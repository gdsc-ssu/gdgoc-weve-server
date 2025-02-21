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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/worries")
public class WorryController {

    private final WorryService worryService;

    /**
     * 고민 작성하기
     */
    @PostMapping
    public BasicResponse<CreateWorryResponse> createWorry(@RequestHeader Long userId,
                                                          @RequestBody @Valid CreateWorryRequest request) {

        CreateWorryResponse response = worryService.createWorry(userId, request);

        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회(JUNIOR ver)
     */
    @GetMapping("/junior")
    public BasicResponse<?> getWorriesForJunior(@RequestHeader Long userId) {
        GetWorriesResponse.juniorVer response = worryService.getWorriesForJunior(userId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회(SENIOR ver)
     */
    @GetMapping("/senior")
    public BasicResponse<?> getWorriesForSenior(@RequestHeader Long userId) {
        GetWorriesResponse.seniorVer response = worryService.getWorriesForSenior(userId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/junior")
    public BasicResponse<?> getWorryForJunior(@RequestHeader Long userId, @PathVariable Long worryId) {
        GetWorryResponse.juniorVer response = worryService.getWorryForJunior(userId, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(SENIOR ver)
     */
    @GetMapping("/{worryId}/senior")
    public BasicResponse<?> getWorryForSenior(@RequestHeader Long userId, @PathVariable Long worryId) {
        GetWorryResponse.seniorVer response = worryService.getWorryForSenior(userId, worryId);
        return BasicResponse.onSuccess(response);
    }
}
