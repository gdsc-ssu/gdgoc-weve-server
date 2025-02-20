package com.weve.controller;

import com.weve.common.api.payload.BasicResponse;
import com.weve.dto.response.SpeechToTextResponse;
import com.weve.service.SttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/stt")
public class SttController {

    private final SttService sttService;

    /**
     * STT(Speech-to-Text) : 오디오 파일을 받아서 텍스트로 변환하여 반환
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BasicResponse<SpeechToTextResponse> handleAudioMessage(@RequestParam MultipartFile audioFile,
                                                                  @RequestParam(defaultValue = "ios") String os) throws IOException {
        int frequency = os.equals("android") ? 44100 : 48000; //샘플링 주파수: ios-48000, android-44100
        String transcribe = sttService.transcribe(audioFile, frequency);

        SpeechToTextResponse response = SpeechToTextResponse.builder()
                .text(transcribe)
                .build();

        return BasicResponse.onSuccess(response);
    }
}
