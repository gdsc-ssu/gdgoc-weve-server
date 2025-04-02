package com.weve.service;

import com.weve.domain.Sms;
import com.weve.domain.User;
import com.weve.repository.SmsRepository;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private final SmsRepository smsRepository;
    private final AuthService authService; // 국가번호 파싱용

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

        // phoneNumber = +82 01000000000의 형태
        // 국가번호 파싱
        String encodedPhone = encodePhoneNumber(phoneNumber); // + → %2B 변환
        log.info("인코딩된 전화번호: {}", encodedPhone);

        String parsedPhoneNumber = authService.extractPhoneNumber(phoneNumber);
        log.info("파싱된 전화번호: " + parsedPhoneNumber);

        String randomNum = createRandomNumber();
        log.info("생성된 인증번호: " + randomNum);

        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(parsedPhoneNumber);
        message.setType(MessageType.SMS);
        message.setText("[인증번호] " + randomNum);

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            log.info("SMS 전송 성공: " + response);

            // 사용자 조회
            if (phoneNumber.startsWith(" ")) {
                phoneNumber = phoneNumber.replaceFirst(" ", "+");
            }
            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
            log.info("입력된 전화번호: " + phoneNumber);
            if (userOpt.isEmpty()) {
                log.warn("해당 전화번호를 가진 유저가 존재하지 않습니다.");
                //return "해당 유저가 존재하지 않습니다.";
            }
            User user = userOpt.get();

            // Sms 정보 업데이트
            Optional<Sms> smsOpt = smsRepository.findByUserPhoneNumber(phoneNumber);
            if (smsOpt.isPresent()) {
                Sms sms = smsOpt.get();
                sms.updatesmsCode(randomNum, LocalDateTime.now().plusMinutes(5));
            } else {
                Sms sms = Sms.builder()
                        .user(user)
                        .smsCode(randomNum)
                        .smsCodeExpiry(LocalDateTime.now().plusMinutes(5))
                        .build();
                smsRepository.save(sms);
            }

            return "문자 전송이 완료되었습니다.";
        } catch (Exception e) {
            log.info("SMS 전송 실패: " + e.getMessage());
            return "문자 전송 실패";
        }
    }

    // 인증번호 검증
    public boolean verifySMSCode(String phoneNumber, String inputCode) {
        if (phoneNumber.startsWith(" ")) {
            phoneNumber = phoneNumber.replaceFirst(" ", "+");
        }
        log.info("인증번호 검증 입력 전화번호: " + phoneNumber);

        Optional<Sms> smsOpt = smsRepository.findByUserPhoneNumber(phoneNumber);
        if (smsOpt.isPresent()) {
            Sms sms = smsOpt.get();
            if (sms.getSmsCode().equals(inputCode) &&
                    sms.getSmsCodeExpiry().isAfter(LocalDateTime.now())) {
                sms.clearSmsCode(); // code, expiry 초기화
                smsRepository.save(sms);
                return true;
            }
        } else {
            log.warn("인증번호 검증: 유저가 존재하지 않습니다.");
        }
        return false;
    }


    // 전화번호 인코딩
    public String encodePhoneNumber(String phoneNumber) {
        return URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8);
    }
}