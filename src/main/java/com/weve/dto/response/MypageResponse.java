package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weve.domain.User;
import com.weve.domain.enums.Language;
import com.weve.domain.enums.ProfileColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MypageResponse {
    private String name;
    private String nationality;
    private String birth;
    private int age;
    private Language language;
    private String phoneNumber;
    private String userType;
    private ProfileColor profileColor;

    public static MypageResponse fromUser(User user) {
        return MypageResponse.builder()
                .name(user.getName())
                .nationality(user.getNationality())
                .birth(user.getBirth() != null ? user.getBirth().toString() : null) // String 변환
                .age(user.getBirth() != null ? calculateAge(user.getBirth()) : 0)
                .language(user.getLanguage())
                .phoneNumber(user.getPhoneNumber())
                .userType(user.getUserType() != null ? user.getUserType().name() : null)
                .profileColor(user.getProfileColor())
                .build();
    }

    // 만 나이 계산 메서드
    private static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}