package com.weve.dto.response;

import com.weve.domain.Appreciate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AppreciateDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private boolean isRead;

    public static AppreciateDto from(Appreciate appreciate) {
        return AppreciateDto.builder()
                .id(appreciate.getId())
                .content(appreciate.getContent())
                .isRead(appreciate.isRead())
                .build();
    }
}