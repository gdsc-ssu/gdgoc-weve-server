package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.User;
import com.weve.domain.Worry;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.CreateWorryResponse;
import com.weve.repository.WorryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorryService {

    private final WorryRepository worryRepository;
    private final UserService userService;

    /**
     * 고민 작성하기
     */
    @Transactional
    public CreateWorryResponse createWorry(Long userId, CreateWorryRequest request) {
        log.info("[고민 작성] userId={}", userId);

        User user = userService.findById(userId);

        // 유저 타입 검사(청년만 고민 작성 가능)
        if(user.isSenior()) {
            throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
        }

        Worry newWorry = Worry.builder()
                .junior(user)
                .content(request.getContent())
                .isAnonymous(request.isAnonymous())
                .build();

        worryRepository.save(newWorry);
        log.info("생성된 고민 ID: {}", newWorry.getId());

        return CreateWorryResponse.builder()
                .worryId(newWorry.getId())
                .build();
    }
}
