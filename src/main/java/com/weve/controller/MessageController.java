package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Basic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/auth/sms")
@Tag(name = "SMS", description = "SMS 관련 API입니다.")
public class MessageController {

    private final MessageService messageService;

    // 인증번호 발송
    @GetMapping("/send")
    public BasicResponse<String> sendSMS(@RequestParam String phone) {  // 사용자가 작성한 번호
        // 예외 처리 한 버전
        try {
            String response = messageService.sendSMS(phone);
            return BasicResponse.onSuccess(response);
        } catch (Exception e) {
            log.error("문자 전송 실패: {}", e.getMessage());
            return BasicResponse.onFailure("400", "문자 전송 실패", null);
        }
    }

    // 인증번호 검증
    @GetMapping("/verify")
    public BasicResponse<Boolean> verifySMSCode(@RequestParam String phone, @RequestParam String code) {
        // 예외 처리 한 버전
        try {
            boolean isValid = messageService.verifySMSCode(phone, code);
            if (isValid) {
                return BasicResponse.onSuccess(true);
            } else {
                return BasicResponse.onFailure("401", "인증 실패", false);
            }
        } catch (Exception e) {
            log.error("문자인증 검증 중 오류 발생: {}", e.getMessage());
            return BasicResponse.onFailure("500", "서버 오류", false);
        }
    }
}
