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
        System.out.println("생성된 인증번호: " + randomNum);

        // SMS 객체 생성
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(phoneNumber);
        message.setType(MessageType.SMS);
        message.setText("[인증번호] " + randomNum);

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println("SMS 전송 성공: " + response);

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
            System.err.println("SMS 전송 실패: " + e.getMessage());
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


// 문자인증 시 들어오는 값은 [국가번호 + 전화번호]
// 국가번호는 3개 중 하나로 파싱 후 이에 따라 국적 분류해야함
// 문자 인증 시 다른 나라 번호도 가능한지 알아봐야함

// 문자인증 연결은 1. 회원가입 2. 전화번호 수정
// mysql에 sms_code 속성 추가한 후 이를 저장한 뒤, 비교해서 같은 값이면 인증 성공 (시간 초과 시 실패)
// 인증 다시 할 때마다 db에 있는 값 갱신

// 1. sms 인증 코드 구현 (mysql 코드 저장 포함)
// 2. 국가번호 파싱 -> 국가 저장 포함 -> sms 인증 연결
// 3. #28 브랜치에서 1번 진행 후 나머지는 2번에서 진행 ???????
