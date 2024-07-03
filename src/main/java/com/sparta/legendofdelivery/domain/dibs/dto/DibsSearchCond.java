package com.sparta.legendofdelivery.domain.dibs.dto;

import com.sparta.legendofdelivery.domain.store.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DibsSearchCond {
    private Category category;

    private Long minDibsCount;

    private Long maxDibsCount;
}
