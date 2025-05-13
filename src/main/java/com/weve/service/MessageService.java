package com.weve.service;

import com.weve.common.api.payload.BasicResponse;
import com.weve.domain.Sms;
import com.weve.domain.User;
import com.weve.domain.enums.ProfileColor;
import com.weve.dto.response.VerificationResponse;
import com.weve.repository.SmsRepository;
import com.weve.repository.UserRepository;
import com.weve.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.weve.domain.enums.UserType.JUNIOR;
import static com.weve.domain.enums.UserType.SENIOR;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final DefaultMessageService messageService; // DefaultMessageService 주입
    private final UserRepository userRepository;
    private final SmsRepository smsRepository;
    private final AuthService authService; // 국가번호 파싱용
    private final JwtUtil jwtUtil;

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
        if (phoneNumber.startsWith(" ")) {
            phoneNumber = phoneNumber.replaceFirst(" ", "+");
        }

        String parsedPhoneNumber = authService.extractPhoneNumber(phoneNumber);
        log.info("파싱된 전화번호: {}", parsedPhoneNumber);

        String randomNum = createRandomNumber();
        log.info("생성된 인증번호: {}", randomNum);

        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(parsedPhoneNumber);
        message.setType(MessageType.SMS);
        message.setText("[인증번호] " + randomNum);

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            log.info("SMS 전송 성공: {}", response);
            return null;
        } catch (Exception e) {
            log.error("SMS 전송 실패: {}", e.getMessage());
            return "문자 전송 실패";
        }
    }

    // 인증번호 검증
    public BasicResponse<VerificationResponse> verifySMSCode(String phoneNumber, String inputCode) {
        boolean isNew = false;
        String token = null; // token 변수 선언 및 초기화

        // 전화번호 형식 정규화
        if (phoneNumber.startsWith(" ")) {
            phoneNumber = phoneNumber.replaceFirst(" ", "+");
        }

        // 사용자 조회
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        User user;

        // 프로필 컬러 추가
        ProfileColor profileColor;
        ProfileColor[] seniorColors = {ProfileColor.ORANGE, ProfileColor.BLUE, ProfileColor.PINK};
        profileColor = seniorColors[new Random().nextInt(seniorColors.length)];

        if (userOpt.isEmpty()) {
            // 사용자가 존재하지 않으면 유저 새로 추가
            user = User.builder()
                    .phoneNumber(phoneNumber)
                    .profileColor(profileColor)
                    .userType(JUNIOR)
                    .build();
            user = userRepository.save(user);
            isNew = true;
        } else {
            user = userOpt.get();
        }

        // 인증번호 정보 조회
        Optional<Sms> smsOpt = smsRepository.findByUserPhoneNumber(phoneNumber);
        Sms sms;

        if (smsOpt.isPresent()) {
            sms = smsOpt.get();
            // 인증번호 및 만료시간 업데이트
            sms = sms.toBuilder()
                    .smsCode(inputCode)
                    .smsCodeExpiry(LocalDateTime.now().plusMinutes(5))
                    .build();
        } else {
            // 새로운 인증번호 정보 생성
            sms = Sms.builder()
                    .user(user)
                    .smsCode(inputCode)
                    .smsCodeExpiry(LocalDateTime.now().plusMinutes(5))
                    .build();
        }
        smsRepository.save(sms);

        // 인증번호 검증
        if (sms.getSmsCode().equals(inputCode) && sms.getSmsCodeExpiry().isAfter(LocalDateTime.now())) {
            // 인증 성공 시, 토큰 생성
            token = jwtUtil.generateToken(user.getPhoneNumber());
        } else {
            log.warn("error: 인증 실패");
            return BasicResponse.onFailure("401", "인증번호가 일치하지 않거나 만료되었습니다.", null);
        }

        VerificationResponse verificationResponse = VerificationResponse.builder()
                .token(token)
                .isNew(isNew)
                .build();

        return BasicResponse.onSuccess(verificationResponse);
    }

    // 전화번호 인코딩
    public String encodePhoneNumber(String phoneNumber) {
        return URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8);
    }
}