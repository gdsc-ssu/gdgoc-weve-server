package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.BasicResponse;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.User;
import com.weve.domain.enums.Language;
import com.weve.dto.request.PatchMypageRequest;
import com.weve.dto.response.MypageResponse;
import com.weve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.weve.domain.enums.UserType.JUNIOR;
import static com.weve.domain.enums.UserType.SENIOR;
import static com.weve.service.AuthService.COUNTRY_NATIONALITY_MAP;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

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
    public BasicResponse<MypageResponse> getMypage(String username) {
        User user = findByPhoneNumber(username);
        MypageResponse response = MypageResponse.fromUser(user);
        return BasicResponse.onSuccess(response);
    }

    // 마이페이지 정보 수정
    @Transactional
    public BasicResponse<MypageResponse> patchMypage(String username, PatchMypageRequest request) {
        User user = findByPhoneNumber(username);

        // 전화번호 국가번호 변경 시 국적도 변경
        String newPhoneNumber = request.getPhoneNumber() != null ? request.getPhoneNumber() : user.getPhoneNumber();

        String newCountryCode = authService.extractCountryCode(newPhoneNumber);
        String newNationality = authService.COUNTRY_NATIONALITY_MAP.getOrDefault(newCountryCode, user.getNationality());

        User patchedUser = user.toBuilder()
                .name(request.getName() != null ? request.getName() : user.getName())
                .birth(request.getBirth() != null ? request.getBirth() : user.getBirth())
                .phoneNumber(newPhoneNumber)
                .language((request.getLanguage() != null ? request.getLanguage() : user.getLanguage()))
                .nationality(newNationality)
                .build();

        userRepository.save(patchedUser);
        MypageResponse response = MypageResponse.fromUser(patchedUser);
        return BasicResponse.onSuccess(response);

    }
}