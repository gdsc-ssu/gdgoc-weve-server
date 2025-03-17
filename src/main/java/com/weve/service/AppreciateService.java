package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.BasicResponse;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.Appreciate;
import com.weve.domain.User;
import com.weve.domain.Worry;
import com.weve.dto.request.PostAppreciateRequest;
import com.weve.dto.response.GetAppreciateListResponse;
import com.weve.dto.response.GetAppreciateResponse;
import com.weve.repository.AppreciateRepository;
import com.weve.repository.WorryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppreciateService {

    private final UserService userService;
    private final WorryRepository worryRepository;
    private final AppreciateRepository appreciateRepository;
    private final WorryService worryService;

    @Transactional
    public void postAppreciate(String username, PostAppreciateRequest request) {

        User user = userService.findByPhoneNumber(username);
        userService.checkIfJunior(user); // 주니어인지 검사

        // worryId에 해당하는 감사인사 내용 저장
        Long worryId = request.getWorryId();

        // 고민 존재 확인
        Worry worry = worryRepository.findById(request.getWorryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 고민을 찾을 수 없습니다."));

        // 고민 작성자 확인
        if (!worry.getJunior().equals(user)) {
            throw new IllegalStateException("자신의 고민에 대해서만 감사 인사를 작성할 수 있습니다.");
        }

        Appreciate appreciate = Appreciate.builder()
                .worry(worry)
                .content(request.getContent())
                .isRead(false) // 기본 값 (안 읽음)
                .build();

        appreciateRepository.save(appreciate);
    }

    // 감사편지 상세 조회 (어르신용)
    @Transactional
    public GetAppreciateResponse.SeniorVer getAppreciate(String username, Long worryId) {

        User user = userService.findByPhoneNumber(username);

        // 유저 타입 검사
        //userService.checkIfSenior(user);

        Worry worry = worryService.findById(worryId);

        // 본인 고민이 아닌 경우, 에러 반환
        if(worry.getJunior() != user) {
            throw new GeneralException(ErrorStatus.WORRY_NOT_MINE);
        }

        // 감사인사가 존재하지 않는 고민일 경우, 에러 반환
        if(worry.getAppreciate() == null) {
            throw new GeneralException(ErrorStatus.WORRY_APPRECIATE_NOT_FOUND);
        }

        Appreciate appreciate = worry.getAppreciate();
        appreciate.isRead = true;  // 조회하면 읽음으로 변경
        appreciateRepository.save(appreciate);

        return GetAppreciateResponse.SeniorVer.builder()
                .content(appreciate.getContent())
                .mp3(appreciate.getAudioUrl())
                .build();
    }

    // 감사편지 목록 조회
    public BasicResponse<GetAppreciateListResponse> getAppreciateList(String username) {

        User user = userService.findByPhoneNumber(username);

        // 읽지 않은 감사편지 조회
        List<Appreciate> newAppreciate = appreciateRepository.findByUserAndIsReadIsFalse(user);

        // 읽은 감사편지 조회
        List<Appreciate> readAppreciate = appreciateRepository.findByUserAndIsReadIsTrue(user);

        GetAppreciateListResponse response = GetAppreciateListResponse.builder()
                .new_appreciate(newAppreciate)
                .read_appreciate(readAppreciate)
                .build();

        return BasicResponse.onSuccess(response);
    }
}
