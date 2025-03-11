package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetWorryResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JuniorVer {
        @Schema(description = "고민 내용", nullable = false, example = "안녕하세요 어르신..")
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
        @Schema(description = "작성자", nullable = false, example = "대한민국에 사는 5세 신짱구")
        private String author;
        @Schema(description = "고민 내용", nullable = false, example = "안녕하세요 어르신..")
        private String content;
        @Schema(description = "음성 파일 URL", nullable = false, example = "...")
        private String audioUrl;
    }
}
