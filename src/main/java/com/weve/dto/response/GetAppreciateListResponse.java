package com.weve.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weve.domain.Appreciate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAppreciateListResponse {

    private List<Appreciate> new_appreciate;
    private List<Appreciate> read_appreciate;
}
