package com.weve.dto.request;

import lombok.Getter;

@Getter
public class CreateWorryRequest {

    private String content;
    private boolean isAnonymous;
}
