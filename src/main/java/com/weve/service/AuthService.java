package com.weve.service;

// 인증 서비스 (로그인하고 jwt 생성)

import com.weve.domain.User;
import com.weve.domain.enums.UserType;
import com.weve.repository.UserRepository;
import com.weve.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    public boolean register(String name, String phoneNumber, LocalDate birth, UserType userType) {

        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);
        if (existingUser.isPresent()) {
            return false;  // 이미 등록된 전화번호
        }

        User newUser = User.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .birth(birth)  // null 허용
                .userType(userType)
                .build();

        userRepository.save(newUser);
        return true;
    }

    // 로그인
    public String login(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> jwtUtil.generateToken(user.getPhoneNumber()))
                .orElse(null);
    }
}
