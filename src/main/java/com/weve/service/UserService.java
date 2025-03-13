package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.BasicResponse;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.MatchingInfo;
import com.weve.domain.User;
import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import com.weve.dto.request.PatchMypageRequest;
import com.weve.dto.request.SeniorInfoRequest;
import com.weve.dto.response.MypageResponse;
import com.weve.dto.response.SeniorProfileResponse;
import com.weve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 어르신 정보 입력
    @Transactional
    public void postSeniorInfo(String username, SeniorInfoRequest request) {

        User user = findByPhoneNumber(username);
        checkIfSenior(user); // 시니어인지 검사 (생략 가능)

        // 정보 받아서 텍스트 분석 후 카테고리 분류 (User 테이블 저장)
        // Gemini 서비스에 있는 analyzeText() 이용
        String newJob = request.getJob();
        String newValue = request.getValue();
        String newHardship = request.getHardship();
        String prompt = newJob + newValue + newHardship;

        // 텍스트 분석 후 카테고리 분류 (세 번 호출하는 방식)
        JobCategory jobResult = geminiService.analyzeText(prompt).getJob();
        ValueCategory valueResult = geminiService.analyzeText(prompt).getValue();
        HardshipCategory hardshipResult = geminiService.analyzeText(prompt).getHardship();
        log.info(String.valueOf(jobResult));
        log.info(String.valueOf(valueResult));
        log.info(String.valueOf(hardshipResult));

        // MatchingInfo 객체 생성
        MatchingInfo matchingInfo = new MatchingInfo(jobResult, valueResult, hardshipResult);

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