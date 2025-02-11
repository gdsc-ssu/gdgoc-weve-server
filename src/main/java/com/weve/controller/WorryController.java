package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.CreateWorryResponse;
import com.weve.dto.response.GetWorriesResponse;
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
                                                          @RequestBody @Valid CreateWorryRequest request){

        CreateWorryResponse response = worryService.createWorry(userId, request);

        return BasicResponse.onSuccess(response);
    }

    /**
     * 고민 목록 조회
     */
    @GetMapping
    public BasicResponse<?> getWorries(@RequestHeader Long userId,
                                       @RequestParam String userType) {

        if (userType.equals("junior")) {
            // JUNIOR ver
            GetWorriesResponse.juniorVer response = worryService.getWorriesForJunior(userId);
            return BasicResponse.onSuccess(response);
        } else {
            // SENIOR ver
            return BasicResponse.onSuccess();
        }
    }
}
