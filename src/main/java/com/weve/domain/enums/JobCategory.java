package com.weve.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JobCategory {
    STABILITY_SEEKING("안정추구형"),
    RISK_TAKING_ADVENTUROUS("모험도전형"),
    TECHNICAL_EXPERTISE("전문기술형"),
    CREATIVE_ARTISTIC("창의예술형"),
    SOCIAL_CONTRIBUTION("사회공헌형");

    private final String description;
}

