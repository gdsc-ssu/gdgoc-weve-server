package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetAnswerResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JuniorVer {
        @Schema(description = "답변 내용", nullable = false, example = "힘내요 젊은이..")
        private String content;
        @Schema(description = "작성자", nullable = false, example = "베트남에 사는 71세 하니팜")
        private String author;
        @Schema(description = "이미지 URL", nullable = true, example = "...")
        private String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SeniorVer {
        @Schema(description = "답변 내용", nullable = false, example = "힘내요 젊은이..")
        private String content;
        @Schema(description = "음성 파일 URL", nullable = false, example = "...")
        private String audioUrl;
        @Schema(description = "이미지 URL", nullable = true, example = "...")
        private String imageUrl;
    }
}
