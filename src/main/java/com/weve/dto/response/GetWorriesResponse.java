package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weve.domain.enums.WorryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetWorriesResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JuniorVer {
        private List<WorryForJunior> worryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorryForJunior {
        @Schema(description = "고민 id", nullable = false, example = "1")
        private Long worryId;
        @Schema(description = "고민 제목", nullable = false, example = "...")
        private String title;
        @Schema(description = "고민 상태", nullable = false, example = "WAITING")
        private WorryStatus status;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SeniorVer {
        private WorryCategoryInfo worryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorryCategoryInfo {
        @Schema(description = "진로")
        private List<WorryForSenior> career;
        @Schema(description = "사랑")
        private List<WorryForSenior> love;
        @Schema(description = "인간관계")
        private List<WorryForSenior> relationship;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WorryForSenior {
        @Schema(description = "고민 id", nullable = false, example = "1")
        private Long worryId;
        @Schema(description = "작성자", nullable = false, example = "익명의 위비")
        private String author;
        @Schema(description = "고민 제목", nullable = false, example = "...")
        private String title;
    }
}
