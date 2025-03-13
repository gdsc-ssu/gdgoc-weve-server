package com.weve.controller;

import com.weve.service.TtsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tts")
@RequiredArgsConstructor
@Tag(name = "TTS", description = "TTS(Text-to-Speech) 관련 API입니다.")
public class TtsController {

    private final TtsService ttsService;

    @GetMapping
    @Operation(
            summary = "[API 연동 X] 텍스트를 음성 파일(URL)로 변환 (TTS)",
            description = "입력된 텍스트를 음성 파일로 변환하고, 해당 파일의 URL을 생성합니다.(고정 음성 파일을 Swagger에서 생성하는 용도)"
    )
    public ResponseEntity<byte[]> convertTextToSpeech(@RequestParam String text) throws Exception {
        byte[] audio = ttsService.convertTextToSpeech(text);
        return ResponseEntity.ok()
                .header("Content-Type", "audio/mpeg")
                .body(audio);
    }
}
