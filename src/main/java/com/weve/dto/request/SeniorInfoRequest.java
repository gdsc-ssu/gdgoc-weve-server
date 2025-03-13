package com.weve.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SeniorInfoRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String job;
    private String value;
    private String hardship;
}
