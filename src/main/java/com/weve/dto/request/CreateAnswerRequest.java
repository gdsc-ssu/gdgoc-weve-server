package com.weve.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAnswerRequest {

    private String content;
    private String imageUrl;
}
