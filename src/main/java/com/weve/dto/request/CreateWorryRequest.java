package com.weve.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateWorryRequest {

    private String content;
    private boolean isAnonymous;
}
