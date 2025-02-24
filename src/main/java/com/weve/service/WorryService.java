package com.weve.service;

import com.weve.common.api.exception.GeneralException;
import com.weve.common.api.payload.code.status.ErrorStatus;
import com.weve.domain.Answer;
import com.weve.domain.MatchingInfo;
import com.weve.domain.User;
import com.weve.domain.Worry;
import com.weve.domain.enums.*;
import com.weve.dto.gemini.ExtractedCategoriesFromText;
import com.weve.dto.request.CreateWorryRequest;
import com.weve.dto.response.CreateWorryResponse;
import com.weve.dto.response.GetAnswerResponse;
import com.weve.dto.response.GetWorriesResponse;
import com.weve.dto.response.GetWorryResponse;
import com.weve.repository.WorryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorryService {

    private final TtsService ttsService;
    private final UserService userService;
    private final GeminiService geminiService;
    private final WorryRepository worryRepository;
    private final GcsService gcsService;

    /**
     * 고민 작성하기
     */
    @Transactional
    public CreateWorryResponse createWorry(Long userId, CreateWorryRequest request) {
        log.info("[고민 작성] userId={}", userId);

        User user = userService.findById(userId);

        // 유저 타입 검사(청년만 고민 작성 가능)
        userService.checkIfJunior(user);

        // 고민 내용을 음성 파일로 변환(TTS)
        byte[] audioData = ttsService.convertTextToSpeech(request.getContent());
        String uuid = gcsService.uploadAudio(audioData);
        String audioUrl = gcsService.processFile(uuid);

        // WorryCategory 추출
        WorryCategory worryCategory = geminiService.analyzeWorry(request.getContent());

        // 제목 생성(1줄 요약)
        String title = geminiService.summarize(request.getContent());

        // 텍스트 분석 및 매칭용 카테고리 추출
        ExtractedCategoriesFromText categories = geminiService.analyzeText(request.getContent());
        MatchingInfo matchingInfo = MatchingInfo.builder()
                .job(categories.getJob())
                .value(categories.getValue())
                .hardship(categories.getHardship())
                .build();

        // Worry 데이터 생성 및 저장
        Worry newWorry = Worry.builder()
                .junior(user)
                .content(request.getContent())
                .title(title)
                .isAnonymous(request.isAnonymous())
                .category(worryCategory)
                .status(WorryStatus.WAITING)
                .audioUrl(audioUrl)
                .matchingInfo(matchingInfo)
                .build();

        worryRepository.save(newWorry);
        log.info("생성된 고민 ID: {}", newWorry.getId());

        return CreateWorryResponse.builder()
                .worryId(newWorry.getId())
                .build();
    }

    /**
     * 고민 목록 조회(JUNIOR ver)
     */
    public GetWorriesResponse.juniorVer getWorriesForJunior(Long userId) {

        log.info("[고민 목록 조회(JUNIOR ver)] userId={}", userId);

        User user = userService.findById(userId);

        // 유저 타입 검사
        userService.checkIfJunior(user);

        List<GetWorriesResponse.WorryForJunior> worries = user.getWorries().stream()
                .map(worry -> {
                    return GetWorriesResponse.WorryForJunior.builder()
                            .worryId(worry.getId())
                            .title(worry.getTitle())
                            .status(worry.getStatus())
                            .build();
                })
                .toList();

        return GetWorriesResponse.juniorVer.builder().worryList(worries).build();
    }

    /**
     * 고민 목록 조회(SENIOR ver)
     */
    public GetWorriesResponse.seniorVer getWorriesForSenior(Long userId) {

        log.info("[고민 목록 조회(SENIOR ver)] userId={}", userId);

        User user = userService.findById(userId);

        // 유저 타입 검사
        userService.checkIfSenior(user);

        // User의 매칭 정보에서 카테고리 정보 가져오기
        MatchingInfo matchingInfo = user.getMatchingInfo();
        JobCategory job = matchingInfo.getJob();
        ValueCategory value = matchingInfo.getValue();
        HardshipCategory hardship = matchingInfo.getHardship();;

        // 고민 목록 조회
        List<Worry> careerWorries = getMatchingWorries(job, value, hardship, WorryCategory.CAREER);
        List<Worry> loveWorries = getMatchingWorries(job, value, hardship, WorryCategory.LOVE);
        List<Worry> relationshipWorries = getMatchingWorries(job, value, hardship, WorryCategory.RELATIONSHIP);

        List<GetWorriesResponse.WorryForSenior> careers = careerWorries.stream()
                .map(worry -> {
                    return GetWorriesResponse.WorryForSenior.builder()
                            .worryId(worry.getId())
                            .author(worry.isAnonymous() ? "위명의 위비" : worry.getJunior().getName())
                            .title(worry.getTitle())
                            .build();
                })
                .toList();

        List<GetWorriesResponse.WorryForSenior> loves = loveWorries.stream()
                .map(worry -> {
                    return GetWorriesResponse.WorryForSenior.builder()
                            .worryId(worry.getId())
                            .author(worry.isAnonymous() ? "위명의 위비" : worry.getJunior().getName())
                            .title(worry.getTitle())
                            .build();
                })
                .toList();

        List<GetWorriesResponse.WorryForSenior> relationships = relationshipWorries.stream()
                .map(worry -> {
                    return GetWorriesResponse.WorryForSenior.builder()
                            .worryId(worry.getId())
                            .author(worry.isAnonymous() ? "위명의 위비" : worry.getJunior().getName())
                            .title(worry.getTitle())
                            .build();
                })
                .toList();

        GetWorriesResponse.WorryCategoryInfo worryCategoryInfo = GetWorriesResponse.WorryCategoryInfo.builder()
                .career(careers)
                .love(loves)
                .relationship(relationships)
                .build();

        return GetWorriesResponse.seniorVer.builder()
                .worryList(worryCategoryInfo)
                .build();
    }

    /**
     * 고민 상세 조회(JUNIOR ver)
     */
    public GetWorryResponse.juniorVer getWorryForJunior(Long userId, Long worryId) {

        log.info("[고민 상세 조회(JUNIOR ver)] userId={}, worryId={}", userId, worryId);

        User user = userService.findById(userId);

        // 유저 타입 검사
        userService.checkIfJunior(user);

        String userDescription = makeAuthorName(user);

        Worry worry = findById(worryId);

        return GetWorryResponse.juniorVer.builder()
                .content(worry.getContent())
                .author(userDescription)
                .build();
    }

    /**
     * 고민 상세 조회(SENIOR ver)
     */
    public GetWorryResponse.seniorVer getWorryForSenior(Long userId, Long worryId) {

        log.info("[고민 상세 조회(SENIOR ver)] userId={}, worryId={}", userId, worryId);

        User user = userService.findById(userId);

        // 유저 타입 검사
        userService.checkIfSenior(user);

        Worry worry = findById(worryId);

        return GetWorryResponse.seniorVer.builder()
                .author(worry.getJunior().getName())
                .nationality(worry.getJunior().getNationality())
                .content(worry.getContent())
                .audioUrl(worry.getAudioUrl())
                .build();
    }

    /**
     * 답변 상세 조회(JUNIOR ver)
     */
    public GetAnswerResponse.juniorVer getAnswerForJunior(Long userId, Long worryId) {

        log.info("[답변 상세 조회(JUNIOR ver)] userId={}, worryId={}", userId, worryId);

        User user = userService.findById(userId);

        // 유저 타입 검사
        userService.checkIfJunior(user);

        Worry worry = findById(worryId);

        // 본인 고민이 아닌 경우, 에러 반환
        if(worry.getJunior() != user) {
            throw new GeneralException(ErrorStatus.WORRY_NOT_MINE);
        }

        // 미답변 고민일 경우, 에러 반환
        if(worry.getAnswer() == null) {
            throw new GeneralException(ErrorStatus.WORRY_UNANSWERED);
        }

        Answer answer = worry.getAnswer();
        String userDescription = makeAuthorName(answer.getSenior());

        return GetAnswerResponse.juniorVer.builder()
                .content(answer.getContent())
                .author(userDescription)
                .imageUrl(answer.getImageUrl())
                .build();
    }

    // 매칭 고민 목록 조회
    private List<Worry> getMatchingWorries(JobCategory job, ValueCategory value, HardshipCategory hardship, WorryCategory category) {
        // 3개 조건 모두 일치하는 Worry들
        List<Worry> threeMatching = worryRepository.findByMatchingInfo_JobAndMatchingInfo_ValueAndMatchingInfo_HardshipAndCategoryAndAnswerIsNull(job, value, hardship, category);

        // 2개 조건: job, value
        List<Worry> twoMatching1 = worryRepository.findByMatchingInfo_JobAndMatchingInfo_ValueAndCategoryAndAnswerIsNull(job, value, category);
        // 2개 조건: job, hardship
        List<Worry> twoMatching2 = worryRepository.findByMatchingInfo_JobAndMatchingInfo_HardshipAndCategoryAndAnswerIsNull(job, hardship, category);
        // 2개 조건: value, hardship
        List<Worry> twoMatching3 = worryRepository.findByMatchingInfo_ValueAndMatchingInfo_HardshipAndCategoryAndAnswerIsNull(value, hardship, category);
        // 2개 조건 결합(Set으로 중복 제거)
        Set<Worry> twoMatching = new HashSet<>();
        twoMatching.addAll(twoMatching1);
        twoMatching.addAll(twoMatching2);
        twoMatching.addAll(twoMatching3);

        // 1개 조건: job
        List<Worry> oneMatching1 = worryRepository.findByMatchingInfo_JobAndCategoryAndAnswerIsNull(job, category);
        // 1개 조건: value
        List<Worry> oneMatching2 = worryRepository.findByMatchingInfo_ValueAndCategoryAndAnswerIsNull(value, category);
        // 1개 조건: hardship
        List<Worry> oneMatching3 = worryRepository.findByMatchingInfo_HardshipAndCategoryAndAnswerIsNull(hardship, category);
        // 1개 조건 결합(Set으로 중복 제거)
        Set<Worry> oneMatching = new HashSet<>();
        oneMatching.addAll(oneMatching1);
        oneMatching.addAll(oneMatching2);
        oneMatching.addAll(oneMatching3);

        // 0개 조건
        List<Worry> zeroMatching = worryRepository.findByCategoryAndAnswerIsNull(category);

        // 랜덤으로 3개 뽑기
        Random random = new Random();
        List<Worry> responseList = new ArrayList<>(3);

        // 각 단계에서 몇 개가 선택되었는지 추적
        int selectedFromThree = 0;
        int selectedFromTwo = 0;
        int selectedFromOne = 0;
        int selectedFromZero = 0;

        // 3개 조건에서 채우기
        while (responseList.size() < 3 && !threeMatching.isEmpty()) {
            int randomIndex = random.nextInt(threeMatching.size());
            Worry selectedItem = threeMatching.remove(randomIndex);
            responseList.add(selectedItem);
            selectedFromThree++;

            // threeMatching 고민들은 아래 조건 리스트에서 제거
            twoMatching.remove(selectedItem);
            oneMatching.remove(selectedItem);
            zeroMatching.remove(selectedItem);
        }

        // 3개 조건이 부족하면 2개 조건을 통해 채우기
        while (responseList.size() < 3 && !twoMatching.isEmpty()) {
            int randomIndex = random.nextInt(twoMatching.size());
            Worry selectedItem = (Worry) twoMatching.toArray()[randomIndex];
            responseList.add(selectedItem); // 선택된 항목 추가
            twoMatching.remove(selectedItem); // 리스트에서 항목 제거
            selectedFromTwo++;

            // twoMatching 고민들은 아래 조건 리스트에서 제거
            oneMatching.remove(selectedItem);
            zeroMatching.remove(selectedItem);
        }

        // 2개 조건이 부족하면 1개 조건을 통해 채우기
        while (responseList.size() < 3 && !oneMatching.isEmpty()) {
            int randomIndex = random.nextInt(oneMatching.size());
            Worry selectedItem = (Worry) oneMatching.toArray()[randomIndex];
            responseList.add(selectedItem); // 선택된 항목 추가
            oneMatching.remove(selectedItem); // 리스트에서 항목 제거
            selectedFromOne++;

            // oneMatching 고민들은 아래 조건 리스트에서 제거
            zeroMatching.remove(selectedItem);
        }

        // 1개 조건이 부족하면 0개 조건을 통해 채우기
        while (responseList.size() < 3 && !zeroMatching.isEmpty()) {
            int randomIndex = random.nextInt(zeroMatching.size());
            Worry selectedItem = zeroMatching.remove(randomIndex);
            responseList.add(selectedItem); // 선택된 항목 추가
            selectedFromZero++;
        }

        // 최신순 정렬
        responseList.sort(Comparator.comparing(Worry::getCreatedAt).reversed());

        log.info("[{} 매칭 목록] 3개 매칭: {}, 2개 매칭: {}, 1개 매칭: {}, 0개 매칭: {}",
                category, selectedFromThree, selectedFromTwo, selectedFromOne, selectedFromZero);

        return responseList;
    }

    // 작성자 소개란 생성
    private String makeAuthorName(User user) {
        return String.format("%s에 사는 %d세 %s",
                user.getNationality(),
                Period.between(user.getBirth(), LocalDate.now()).getYears(),
                user.getName()
        );
    }

    // id로 Worry 검색
    public Worry findById(Long worryId) {
        return worryRepository.findById(worryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WORRY_NOT_FOUND));
    }
}
