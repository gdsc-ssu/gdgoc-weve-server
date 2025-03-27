package com.weve.service;

import com.weve.domain.User;
import com.weve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final DefaultMessageService messageService; // DefaultMessageService 주입
    private final UserRepository userRepository;

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    // 인증번호 생성
    private String createRandomNumber() {
        Random rand = new Random();
        StringBuilder randomNum = new StringBuilder();
        for (int i = 0; i < 6; i++) { // 6자리 인증번호 생성
            randomNum.append(rand.nextInt(10));
        }
        return randomNum.toString();
    }

    // 인증번호 전송 & MySQL에 저장
    @Transactional
    public String sendSMS(String phoneNumber) {
        String randomNum = createRandomNumber();
        log.info("생성된 인증번호: " + randomNum);

        // SMS 객체 생성
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(phoneNumber);
        message.setType(MessageType.SMS);
        message.setText("[인증번호] " + randomNum);

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            log.info("SMS 전송 성공: " + response);

            // MySQL에 인증번호 저장 (기존 번호 갱신)
            Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
            if (user.isPresent()) {
                User updatedUser = user.get().toBuilder()
                        .smsCode(randomNum)
                        .smsCodeExpiry(LocalDateTime.now().plusMinutes(5))  // 5분제한
                        .build();
                userRepository.save(updatedUser);
            } else {
                User newUser = User.builder()
                        .phoneNumber(phoneNumber)
                        .smsCode(randomNum)
                        .smsCodeExpiry(LocalDateTime.now().plusMinutes(5))
                        .build();
                userRepository.save(newUser);
            }

            return "문자 전송이 완료되었습니다.";
        } catch (Exception e) {
            log.info("SMS 전송 실패: " + e.getMessage());
            return "문자 전송 실패";
        }
    }

    // 인증번호 검증
    public boolean verifySMSCode(String phoneNumber, String inputCode) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            User foundUser = user.get();
            if (foundUser.getSmsCode().equals(inputCode) &&
                    foundUser.getSmsCodeExpiry().isAfter(LocalDateTime.now())) {
                User updatedUser = foundUser.toBuilder()
                        .smsCode(null)
                        .smsCodeExpiry(null)
                        .build();
                userRepository.save(updatedUser);
                return true;
            }
        }
        return false;
    }
}