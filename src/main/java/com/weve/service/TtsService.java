package com.weve.service;

import com.google.cloud.texttospeech.v1.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TtsService {

    private final TextToSpeechClient textToSpeechClient;

    /**
     * https://cloud.google.com/text-to-speech/docs/create-audio?hl=ko
     */
    public byte[] convertTextToSpeech(String text) {

        //변환할 텍스트 설정
        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode("ko-KR") //언어 코드
                .setSsmlGender(SsmlVoiceGender.FEMALE) //성별
                .build();

        //음성 출력 형식(MP3) 설정
        AudioConfig audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3).build();

        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
        return response.getAudioContent().toByteArray();
    }
}