package com.weve.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpeechToTextResponse {
    @Schema(description = "변환된 텍스트", nullable = false, example = "...")
    private String text;
}
