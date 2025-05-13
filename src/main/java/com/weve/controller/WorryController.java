package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.request.CreateAnswerRequest;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.*;
import com.weve.service.AnswerService;
import com.weve.service.WorryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Worry", description = "Worry 관련 API입니다.")
public class WorryController {

    private final WorryService worryService;
    private final AnswerService answerService;

    /**
     * 고민 작성하기
     */
    @PostMapping
    @Operation(summary = "고민 작성하기", description = "고민을 작성합니다.")
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
    @Operation(summary = "고민 목록 조회(JUNIOR ver)", description = "고민 목록을 조회합니다.(JUNIOR ver)")
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = GetWorriesResponse.JuniorVer.class))
    )
    public BasicResponse<GetWorriesResponse.JuniorVer> getWorriesForJunior(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        GetWorriesResponse.JuniorVer response = worryService.getWorriesForJunior(username);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회(SENIOR ver)
     */
    @GetMapping("/senior")
    @Operation(summary = "고민 목록 조회(SENIOR ver)", description = "고민 목록을 조회합니다.(SENIOR ver)")
    public BasicResponse<GetWorriesResponse.SeniorVer> getWorriesForSenior(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        GetWorriesResponse.SeniorVer response = worryService.getWorriesForSenior(username);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/junior")
    @Operation(summary = "고민 상세 조회(JUNIOR ver)", description = "고민을 상세 조회합니다.(JUNIOR ver)")
    public BasicResponse<GetWorryResponse.JuniorVer> getWorryForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                                                       @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetWorryResponse.JuniorVer response = worryService.getWorryForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 상세 조회(SENIOR ver)
     */
    @GetMapping("/{worryId}/senior")
    @Operation(summary = "고민 상세 조회(SENIOR ver)", description = "고민을 상세 조회합니다.(SENIOR ver)")
    public BasicResponse<GetWorryResponse.SeniorVer> getWorryForSenior(@AuthenticationPrincipal UserDetails userDetails,
                                                                       @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetWorryResponse.SeniorVer response = worryService.getWorryForSenior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 답변 작성하기
     */
    @PostMapping("/{worryId}/answer")
    @Operation(summary = "답변 작성하기", description = "답변을 작성합니다.")
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
    @Operation(summary = "답변 상세 조회(JUNIOR ver)", description = "답변을 상세 조회합니다.(JUNIOR ver)")
    public BasicResponse<GetAnswerResponse.JuniorVer> getAnswerForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                                                         @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetAnswerResponse.JuniorVer response = worryService.getAnswerForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 답변 상세 조회(SENIOR ver)
     */
    @GetMapping("/{worryId}/answer/senior")
    @Operation(summary = "답변 상세 조회(SENIOR ver)", description = "답변을 상세 조회합니다.(SENIOR ver)")
    public BasicResponse<GetAnswerResponse.SeniorVer> getAnswerForSenior(@AuthenticationPrincipal UserDetails userDetails,
                                                                         @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetAnswerResponse.SeniorVer response = worryService.getAnswerForSenior(username, worryId);
        return BasicResponse.onSuccess(response);
    }

    /**
     * 감사편지 상세 조회(JUNIOR ver)
     */
    @GetMapping("/{worryId}/appreciate/junior")
    @Operation(summary = "감사편지 상세 조회(JUNIOR ver)", description = "감사편지를 상세 조회합니다.(JUNIOR ver)")
    public BasicResponse<GetAppreciateResponse.JuniorVer> getAppreciateForJunior(@AuthenticationPrincipal UserDetails userDetails,
                                                                                 @PathVariable Long worryId) {
        String username = userDetails.getUsername();
        GetAppreciateResponse.JuniorVer response = worryService.getAppreciateForJunior(username, worryId);
        return BasicResponse.onSuccess(response);
    }
}
