package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weve.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationResponse {
    private String token;
    private Boolean isNew;

    public VerificationResponse(String token, AtomicBoolean isNew) {
    }
}
