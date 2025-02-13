package com.weve.dto.response;

import com.weve.domain.enums.WorryStatus;
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
    public static class juniorVer {
        private List<WorryForJunior> worryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorryForJunior {
        private Long worryId;
        private String title;
        private WorryStatus status;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class seniorVer {
        private WorryCategoryInfo worryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorryCategoryInfo {
        private List<WorryForSenior> career;
        private List<WorryForSenior> love;
        private List<WorryForSenior> relationship;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorryForSenior {
        private Long worryId;
        private String author;
        private String title;
    }
}
