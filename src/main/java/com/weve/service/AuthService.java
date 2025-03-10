package com.weve.service;

// 인증 서비스 (로그인하고 jwt 생성)

import com.weve.domain.User;
import com.weve.domain.enums.Language;
import com.weve.domain.enums.UserType;
import com.weve.repository.UserRepository;
import com.weve.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 회원가입
    public boolean register(String name, String phoneNumber, LocalDate birth, UserType userType, Language language) {

        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);
        if (existingUser.isPresent()) {
            return false;  // 이미 등록된 전화번호
        }

        // 전화번호에서 국가번호 파싱
        String countryCode = extractCountryCode(phoneNumber);
        String nationality = COUNTRY_NATIONALITY_MAP.getOrDefault(countryCode, "Unknown");

        User newUser = User.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .birth(birth)  // null 허용
                .userType(userType)
                .language(language)
                .nationality(nationality)
                .build();

        userRepository.save(newUser);
        return true;
    }

    // 로그인
    public String login(String phoneNumber, String name) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .filter(user -> user.getName().equals(name)) // 이름 검증 추가
                .map(user -> jwtUtil.generateToken(user.getPhoneNumber()))
                .orElse(null);
    }

    // 국가 번호별 국적 매핑
    public static final Map<String, String> COUNTRY_NATIONALITY_MAP = Map.of(
            "+82", "South Korea",  // 대한민국
            "+1", "United States", // 미국
            "+81", "Japan"         // 일본
    );

    // 국가 코드 추출
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+\\d{1,3})\\s?\\d+");

    public String extractCountryCode(String phoneNumber) {
        Matcher matcher = PHONE_PATTERN.matcher(phoneNumber);
        if (matcher.find()) {
            return matcher.group(1); // 국가 코드 반환
        }
        return ""; // 기본값
    }
}