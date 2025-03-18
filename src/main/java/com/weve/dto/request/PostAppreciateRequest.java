package com.weve.dto.request;

import lombok.Getter;

@Getter
public class PostAppreciateRequest {

    private Long worryId;
    private String content; // 감사인사 내용
}
