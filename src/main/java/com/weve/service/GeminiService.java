package com.weve.service;

import com.weve.dto.gemini.ChatRequest;
import com.weve.dto.gemini.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    public String chat(String prompt) {

        String requestUrl = apiUrl + "?key=" + geminiApiKey;




        ChatRequest request = ChatRequest.builder()
                .contents(List.of(ChatRequest.Content.builder()
                        .parts(List.of(ChatRequest.Part.builder().text(prompt).build()))
                        .build()))
                .build();

        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }
}
