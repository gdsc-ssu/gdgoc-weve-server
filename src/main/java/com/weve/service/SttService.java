package com.weve.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SttService {

    private final SpeechSettings speechSettings;

    public String transcribe(MultipartFile audioFile, int frequency) throws IOException {

        // 오디오 파일을 byte array로 decode
        byte[] audioBytes = audioFile.getBytes();
        ByteString audioData = ByteString.copyFrom(audioBytes);

        // 설정 객체 생성
        RecognitionConfig recognitionConfig =
                RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.FLAC) //파일은 FLAC 형식
                        .setSampleRateHertz(frequency) //아이폰 48000, 안드로이드 44100
                        .setLanguageCode("ko-kR") //영어 en-US, 한국어 ko-KR
                        .build();

        // 오디오 객체 생성
        RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                .setContent(audioData)
                .build();

        // 클라이언트 인스턴스화
        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {

            // 오디오-텍스트 변환 수행
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcript.append(alternative.getTranscript());
            }
            return transcript.toString();
        }
    }
}
