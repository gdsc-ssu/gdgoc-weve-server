package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.Answer;
import com.weve.domain.User;
import com.weve.domain.Worry;
import com.weve.dto.request.CreateAnswerRequest;
import com.weve.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final WorryService worryService;
    private final UserService userService;
    private final TtsService ttsService;
    private final GcsService gcsService;

    /**
     * 답변 작성하기
     */
    @Transactional
    public void createAnswer(Long userId, Long worryId, CreateAnswerRequest request) {
        log.info("[답변 작성] userId={}, worryId={}", userId, worryId);

        User user = userService.findById(userId);
        Worry worry = worryService.findById(worryId);

        // 이미 답변이 달린 고민일 경우, 에러 반환
        if(worry.getAnswer() != null) {
            throw new GeneralException(ErrorStatus.WORRY_ALREADY_ANSWERED);
        }

        // 유저 타입 검사(어르신만 답변 작성 가능)
        userService.checkIfSenior(user);

        // 고민 내용을 음성 파일로 변환(TTS)
        byte[] audioData = ttsService.convertTextToSpeech(request.getContent());
        String uuid = gcsService.uploadAudio(audioData);
        String audioUrl = gcsService.processFile(uuid);

        // Answer 데이터 생성 및 저장
        Answer newAnswer = Answer.builder()
                .senior(user)
                .worry(worry)
                .answer(request.getContent())
                .imageUrl(request.getImageUrl())
                .audioUrl(audioUrl)
                .build();

        answerRepository.save(newAnswer);
        log.info("생성된 답변 ID: {}", newAnswer.getId());
    }
}
