package com.weve.service;

import com.weve.domain.Appreciate;
import com.weve.domain.User;
import com.weve.domain.Worry;
import com.weve.dto.request.PostAppreciateRequest;
import com.weve.repository.AppreciateRepository;
import com.weve.repository.WorryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppreciateService {

    private final UserService userService;
    private final WorryRepository worryRepository;
    private final AppreciateRepository appreciateRepository;

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
        log.info("감사 인사 저장 완료: worryId={}, appreciateId={}", worry.getId(), appreciate.getId());
    }
}
