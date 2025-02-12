package com.weve.domain;

import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfo {

    @Enumerated(EnumType.STRING)
    private JobCategory job;

    @Enumerated(EnumType.STRING)
    private ValueCategory value;

    @Enumerated(EnumType.STRING)
    private HardshipCategory hardship;
}
