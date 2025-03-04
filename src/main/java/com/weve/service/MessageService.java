package com.weve.service;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
//import net.nurigo.sdk.message.exception.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public String sendSMS(String phoneNumber) {
        String randomNum = createRandomNumber();
        System.out.println("인증번호: " + randomNum);

        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(phoneNumber);
        message.setType(MessageType.SMS);
        message.setText("[인증번호] " + randomNum);

//        try {
//            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
//            System.out.println("문자 전송 성공: " + response);
//        } catch (CoolsmsException e) {
//            System.err.println("문자 전송 실패: " + e.getErrorMessage());
//            return "문자 전송 실패";
//        }

        return "문자 전송이 완료되었습니다.";
    }
}
