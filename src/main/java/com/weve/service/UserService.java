package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.BasicResponse;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.MatchingInfo;
import com.weve.domain.User;
import com.weve.domain.enums.*;
import com.weve.dto.gemini.ExtractedCategoriesFromText;
import com.weve.dto.request.PatchMypageRequest;
import com.weve.dto.request.SeniorInfoRequest;
import com.weve.dto.response.MypageResponse;
import com.weve.dto.response.SeniorProfileResponse;
import com.weve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.weve.domain.enums.UserType.JUNIOR;
import static com.weve.domain.enums.UserType.SENIOR;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final GeminiService geminiService;

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
//        String newNationality = authService.COUNTRY_NATIONALITY_MAP.getOrDefault(newCountryCode, user.getNationality());

        String newNationality = AuthService.COUNTRY_NATIONALITY_MAP.containsKey(newCountryCode)
                ? AuthService.COUNTRY_NATIONALITY_MAP.get(newCountryCode)
                : user.getNationality();

        // 프로필 이미지 설정을 위한 유저 타입 검사
        ProfileColor profileColor;
        if (user.getProfileColor() == null) {
            if (user.getUserType() == UserType.SENIOR) {
                // 시니어: ORANGE, BLUE, PINK 중 랜덤 선택
                ProfileColor[] seniorColors = {ProfileColor.ORANGE, ProfileColor.BLUE, ProfileColor.PINK};
                profileColor = seniorColors[new Random().nextInt(seniorColors.length)];
            } else {
                // 주니어: YELLOW, GREEN 중 랜덤 선택
                ProfileColor[] juniorColors = {ProfileColor.YELLOW, ProfileColor.GREEN};
                profileColor = juniorColors[new Random().nextInt(juniorColors.length)];
            }
        } else profileColor = user.getProfileColor();

        User patchedUser = user.toBuilder()
                .name(request.getName() != null ? request.getName() : user.getName())
                .birth(request.getBirth() != null ? request.getBirth() : user.getBirth())
                .phoneNumber(newPhoneNumber)
                .language((request.getLanguage() != null ? request.getLanguage() : user.getLanguage()))
                .nationality(newNationality)
                .profileColor(profileColor)
                .userType(user.getUserType())
                .build();

        userRepository.save(patchedUser);
        MypageResponse response = MypageResponse.fromUser(patchedUser);
        return BasicResponse.onSuccess(response);
    }

    // 어르신 정보 입력
    @Transactional
    public void postSeniorInfo(String username, SeniorInfoRequest request) {

        User user = findByPhoneNumber(username);
        checkIfSenior(user); // 시니어인지 검사 (생략 가능)

        // 정보 받아서 텍스트 분석 후 카테고리 분류 (User 테이블 저장)
        // Gemini 서비스에 있는 analyzeText() 이용
        String prompt = request.getJob() + request.getValue() + request.getHardship();
        ExtractedCategoriesFromText categories = geminiService.analyzeText(prompt);

        // MatchingInfo 객체 생성
        MatchingInfo matchingInfo = MatchingInfo.builder()
                .job(categories.getJob())
                .value(categories.getValue())
                .hardship(categories.getHardship())
                .build();

        log.info(matchingInfo.toString());

        // 분석된 것을 User 테이블에 저장
        User newUser = user.toBuilder()
                .birth(request.getBirth())
                .matchingInfo(matchingInfo)
                .build();

        // 변경된 User 엔티티 저장
        userRepository.save(newUser);
    }

    // 어르신 정보 확인
    public BasicResponse<SeniorProfileResponse> getSeniorInfo(String username) {

        User user = findByPhoneNumber(username);
        SeniorProfileResponse response = SeniorProfileResponse.fromUser(user);
        return BasicResponse.onSuccess(response);
    }
}