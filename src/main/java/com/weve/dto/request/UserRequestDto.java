package com.weve.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weve.domain.enums.Language;
import com.weve.domain.enums.UserType;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String name;
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // JSON → LocalDate 변환
    private LocalDate birth;
    private UserType userType;
    private Language language;
}
