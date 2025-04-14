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
    private Boolean hasWrittenBasicInfo;

    public static SeniorProfileResponse fromUser(User user) {

        // 기본정보 작성 여부 반환
        boolean hasWrittenBasicInfo = user.getMatchingInfo() != null
                && user.getMatchingInfo().getJob() != null
                && user.getMatchingInfo().getValue() != null
                && user.getMatchingInfo().getHardship() != null;

        return SeniorProfileResponse.builder()
                .name(user.getName())
                .hasWrittenBasicInfo(hasWrittenBasicInfo)
                .build();
    }
}
