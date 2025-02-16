package com.weve.service;

// 인증 서비스 (로그인하고 jwt 생성)

import com.weve.domain.User;
import com.weve.repository.UserRepository;
import com.weve.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String name, String phoneNumber) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getName().equals(name)) {
                return jwtUtil.generateToken(phoneNumber);
            }
        } else {
            // 새 사용자 등록
            User newUser = User.builder().name(name).phoneNumber(phoneNumber).build();
            userRepository.save(newUser);
            return jwtUtil.generateToken(phoneNumber);
        }

        throw new IllegalArgumentException("이름 또는 전화번호가 잘못되었습니다.");
    }
}
