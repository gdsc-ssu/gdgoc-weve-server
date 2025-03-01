package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.request.CreateAnswerRequest;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.*;
import com.weve.service.AnswerService;
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
    private final AnswerService answerService;

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
    public BasicResponse<GetWorriesResponse.juniorVer> getWorriesForJunior(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        GetWorriesResponse.juniorVer response = worryService.getWorriesForJunior(username);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회(SENIOR ver)
     */
    @GetMapping("/senior")
    public BasicResponse<GetWorriesResponse.seniorVer> getWorriesForSenior(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        GetWorriesResponse.seniorVer response = worryService.getWorriesForSenior(username);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/junior")
    public BasicResponse<GetWorryResponse.juniorVer> getWorryForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                                                       @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetWorryResponse.juniorVer response = worryService.getWorryForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(SENIOR ver)
     */
    @GetMapping("/{worryId}/senior")
    public BasicResponse<GetWorryResponse.seniorVer> getWorryForSenior(@AuthenticationPrincipal UserDetails userDetails,
                                                                       @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetWorryResponse.seniorVer response = worryService.getWorryForSenior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 답변 작성하기
     */
    @PostMapping("/{worryId}/answer")
    public BasicResponse<?> createAnswer(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long worryId,
                                         @RequestBody @Valid CreateAnswerRequest request) {

        String username = userDetails.getUsername();
        answerService.createAnswer(username, worryId, request);

        return BasicResponse.onSuccess(null);
    }

    /**
     * 답변 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/answer/junior")
    public BasicResponse<GetAnswerResponse.juniorVer> getAnswerForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                                                         @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetAnswerResponse.juniorVer response = worryService.getAnswerForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 답변 상세 조회(SENIOR ver)
     */
    @GetMapping("/{worryId}/answer/senior")
    public BasicResponse<GetAnswerResponse.seniorVer> getAnswerForSenior(@AuthenticationPrincipal UserDetails userDetails,
                                                                         @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetAnswerResponse.seniorVer response = worryService.getAnswerForSenior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 감사편지 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/appreciate/junior")
    public BasicResponse<GetAppreciateResponse.juniorVer> getAppreciateForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                                                                 @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetAppreciateResponse.juniorVer response = worryService.getAppreciateForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }
}
