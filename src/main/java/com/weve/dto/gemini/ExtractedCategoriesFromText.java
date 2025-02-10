package com.weve.dto.gemini;

import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedCategoriesFromText {
    private JobCategory job;
    private ValueCategory value;
    private HardshipCategory hardship;
}
