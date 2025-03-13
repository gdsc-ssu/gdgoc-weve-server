package com.weve.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAnswerRequest {

    @Schema(description = "답변 내용", nullable = false, example = "힘내요 젊은이..")
    private String content;
    @Schema(description = "이미지 URL", nullable = true, example = "...")
    private String imageUrl;
}
