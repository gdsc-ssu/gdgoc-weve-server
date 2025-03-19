package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetAppreciateResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JuniorVer {

        @Schema(description = "감사편지 내용", nullable = false, example = "감사합니다 어르신..")
        private String content;
        @Schema(description = "작성자", nullable = false, example = "대한민국에 사는 5세 신짱구")
        private String author;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SeniorVer {
        @Schema(description = "감사편지 내용", nullable = false, example = "감사합니다 어르신..")
        private String content;
        @Schema(description = "mp3_url", nullable = false)
        private String mp3;
    }
}
