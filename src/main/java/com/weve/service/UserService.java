package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.BasicResponse;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.User;
import com.weve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import static com.weve.domain.enums.UserType.JUNIOR;
import static com.weve.domain.enums.UserType.SENIOR;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // id로 유저 검색
    public User findById(Long memberId) {
        return userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    // 전화번호로 유저 검색
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    // 주니어인지 검사
    public void checkIfJunior(User user) {
        if(user.getUserType() != JUNIOR) {
            throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
        }
    }

    // 시니어인지 검사
    public void checkIfSenior(User user) {
        if(user.getUserType() != SENIOR) {
            throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
        }
    }

    // 마이페이지 정보 조회
    public BasicResponse<?> getMypage(String username) {

        User user = userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 생년월일을 LocalDate로 변환
        LocalDate birthDate = user.getBirth();
        int age = calculateAge(birthDate);

        Map<String, Object> result = new HashMap<>();
        result.put("name", user.getName());
        result.put("nationality", user.getNationality());
        result.put("birth", birthDate.toString());
        result.put("age", age);
        result.put("language", user.getLanguage());

        return BasicResponse.onSuccess(result);
    }

    // 나이 계산 메서드 (만 나이 기준)
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}