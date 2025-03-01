package com.weve.service;

// 인증 서비스 (로그인하고 jwt 생성)

import com.weve.domain.User;
import com.weve.domain.enums.Language;
import com.weve.domain.enums.UserType;
import com.weve.repository.UserRepository;
import com.weve.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

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

        User newUser = User.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .birth(birth)  // null 허용
                .userType(userType)
                .language(language)
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
}
