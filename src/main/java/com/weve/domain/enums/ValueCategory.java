package com.weve.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ValueCategory {
    ACHIEVEMENT("성취지향형"),
    RELATIONSHIP("인간관계형"),
    SELF_DEVELOPMENT("자기개발형"),
    FREEDOM_INDEPENDENCE("자유독립형"),
    STABILITY_PEACE("안정평온형");

    private final String description;
}
