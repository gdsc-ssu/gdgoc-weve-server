package com.weve.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateWorryRequest {

    @Schema(description = "고민 내용", nullable = false, example = "안녕하세요 어르신..")
    private String content;

    @Getter(AccessLevel.NONE)
    @Schema(description = "익명 여부", nullable = false, example = "true")
    private boolean isAnonymous;

    public boolean getIsAnonymous() {
        return isAnonymous;
    }
}
