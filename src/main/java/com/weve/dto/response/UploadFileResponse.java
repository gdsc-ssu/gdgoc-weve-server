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
public class UploadFileResponse {
    @Schema(description = "파일 URL", nullable = false, example = "...")
    private String fileUrl;
}

