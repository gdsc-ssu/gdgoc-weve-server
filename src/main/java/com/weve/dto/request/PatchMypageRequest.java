package com.weve.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weve.domain.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
public class PatchMypageRequest {
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // JSON → LocalDate 변환
    private LocalDate birth;
    private String phoneNumber;
    private Language language;
}
