package com.weve.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import com.weve.domain.enums.WorryCategory;
import com.weve.dto.gemini.GeminiRequest;
import com.weve.dto.gemini.GeminiResponse;
import com.weve.dto.gemini.ExtractedCategoriesFromText;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeminiService {

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    /**
     * 텍스트 분석 후, 카테고리(mapping category) 추출
     */
    public ExtractedCategoriesFromText analyzeText(String prompt) {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        // 프롬프트 확장
        String extendedPrompt = prompt + "\n\n"
                + "Analyze the given text and determine the most suitable category from each of the following three groups:\n"
                + "1. JobCategory (Choose one from: " + Arrays.toString(JobCategory.values()) + ")\n"
                + "2. ValueCategory (Choose one from: " + Arrays.toString(ValueCategory.values()) + ")\n"
                + "3. HardshipCategory (Choose one from: " + Arrays.toString(HardshipCategory.values()) + ")\n"
                + "Respond with only the JSON format:\n"
                + "{ \"job\": \"CATEGORY_NAME\", \"value\": \"CATEGORY_NAME\", \"hardship\": \"CATEGORY_NAME\" }";

        // Gemeni 요청
        GeminiRequest request = GeminiRequest.builder()
                .contents(List.of(GeminiRequest.Content.builder()
                        .parts(List.of(GeminiRequest.Part.builder().text(extendedPrompt).build()))
                        .build()))
                .build();

        // Gemeni 응답 : 텍스트 분석 -> 3가지 카테고리 값 추출 -> JSON 형식으로 반환
        GeminiResponse response = restTemplate.postForObject(requestUrl, request, GeminiResponse.class);
        String result = response.getCandidates().get(0).getContent().getParts().get(0).getText();
        String cleanedResult = result.replaceAll("^```json|```$", "").trim(); // 마크다운 문법 제거 후, 순수 JSON만 반환

        log.info("추출된 매칭 정보: {}", cleanedResult);

        // JSON 문자열을 DTO로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(cleanedResult, ExtractedCategoriesFromText.class);
        } catch (JsonProcessingException e) {
            log.error("JSON 응답 처리 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 고민 내용 분석 후, 카테고리(worry category) 추출
     */
    public WorryCategory analyzeWorry(String prompt) {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        // 프롬프트 확장
        String extendedPrompt = prompt + "\n\n"
                + "Analyze the given text and determine the most suitable category from each of the following group:\n"
                + "Choose one from: " + Arrays.toString(WorryCategory.values()) + ")\n"
                + "Return only one category name without any additional text.";

        // Gemeni 요청
        GeminiRequest request = GeminiRequest.builder()
                .contents(List.of(GeminiRequest.Content.builder()
                        .parts(List.of(GeminiRequest.Part.builder().text(extendedPrompt).build()))
                        .build()))
                .build();

        // Gemeni 응답 : 텍스트 분석 -> 카테고리 값 추출 -> JSON 형식으로 반환
        GeminiResponse response = restTemplate.postForObject(requestUrl, request, GeminiResponse.class);
        String result = response.getCandidates().get(0).getContent().getParts().get(0).getText();

        log.info("추출된 고민 카테고리: {}", result);

        return WorryCategory.valueOf(result.trim());
    }

    /**
     * 10~15자 내 1줄 요약(고민 내용 분석 후, 제목 생성)
     */
    public String summarize(String prompt) {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        // 프롬프트 확장
        String extendedPrompt = prompt + "\n\n"
                + "Summarize the given text into a single sentence in a natural speaking tone, using polite language"
                + "Keep it between 10 to 15 characters."
                + "Return only the summary without any additional text and without a period at the end";

        // Gemini 요청 생성
        GeminiRequest request = GeminiRequest.builder()
                .contents(List.of(GeminiRequest.Content.builder()
                        .parts(List.of(GeminiRequest.Part.builder().text(extendedPrompt).build()))
                        .build()))
                .build();

        // Gemini 응답 처리
        GeminiResponse response = restTemplate.postForObject(requestUrl, request, GeminiResponse.class);
        String result = response.getCandidates().get(0).getContent().getParts().get(0).getText();

        log.info("Gemini 응답: {}", result);

        return result.trim();
    }
}
