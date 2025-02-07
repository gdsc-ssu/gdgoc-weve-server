package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.User;
import com.weve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
