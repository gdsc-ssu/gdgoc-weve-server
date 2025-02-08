package com.weve.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HardshipCategory {
    LOGICAL_PROBLEM_SOLVING("논리적 해결형"),
    EMOTIONAL_SUPPORT("감정적 공감형"),
    PERSISTENCE_ENDURANCE("끈기 인내형"),
    CREATIVE_TRANSFORMATION("창의적 변환형"),
    IMMEDIATE_ACTION("즉각적 실행형");

    private final String description;
}