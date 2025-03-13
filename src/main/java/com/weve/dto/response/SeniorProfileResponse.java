package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weve.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeniorProfileResponse {

    private String name;
    private String nationality;

    public static SeniorProfileResponse fromUser(User user) {
        return SeniorProfileResponse.builder()
                .name(user.getName())
                .nationality(user.getNationality())
                .build();
    }
}
