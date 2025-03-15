package com.weve.dto.gemini;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeminiResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

    @Getter
    @Builder
    public static class Candidate {
        private Content content;
        private String finishReason;
        private double avgLogprobs;
    }

    @Getter
    @Builder
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @Getter
    @Builder
    @NoArgsConstructor  // 기본 생성자 추가 (역직렬화 오류 해결)
    @AllArgsConstructor
    public static class Part {
        private String text;
    }

    @Getter
    @Builder
    public static class UsageMetadata {
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;
        private List<TokensDetail> promptTokensDetails;
        private List<TokensDetail> candidatesTokensDetails;
    }

    @Getter
    @Builder
    public static class TokensDetail {
        private String modality;
        private int tokenCount;
    }
}

