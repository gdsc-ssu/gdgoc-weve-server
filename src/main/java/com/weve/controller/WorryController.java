package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.CreateWorryResponse;
import com.weve.service.WorryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/worries")
public class WorryController {

    private final WorryService worryService;

    @PostMapping
    public BasicResponse<CreateWorryResponse> createWorry(@RequestHeader Long userId,
                                                          @RequestBody @Valid CreateWorryRequest request){

        CreateWorryResponse response = worryService.createWorry(userId, request);

        return BasicResponse.onSuccess(response);
    }
}
