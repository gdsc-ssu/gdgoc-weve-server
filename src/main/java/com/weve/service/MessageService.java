package com.weve.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class MessageService {

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private String createRandomNumber() {
        Random rand = new Random();
        StringBuilder randomNum = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            randomNum.append(rand.nextInt(10));
        }
        return randomNum.toString();
    }

    private HashMap<String, String> makeParams(String to, String randomNum) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("app_version", "test app 1.2");
        params.put("to", to);
        params.put("text", randomNum);
        return params;
    }

    // 인증번호 전송하기
    public String sendSMS(String phoneNumber) {
        Message coolsms = new Message(apiKey, apiSecret);

        // 랜덤한 인증 번호 생성
        String randomNum = createRandomNumber();
        System.out.println("인증번호: " + randomNum);

        // 발신 정보 설정
        HashMap<String, String> params = makeParams(phoneNumber, randomNum);

        try {
            JSONObject obj = coolsms.send(params);
            System.out.println(obj.toJSONString());
        } catch (CoolsmsException e) {
            System.err.println("문자 전송 실패: " + e.getMessage());
            return "문자 전송 실패";
        }

        return "문자 전송이 완료되었습니다.";
    }
}
