package com.sparta.legendofdelivery.domain.dibs.repository;

import com.querydsl.core.Tuple;
import com.sparta.legendofdelivery.domain.dibs.dto.DibsSearchCond;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DibsRepositoryCustom {
    List<Tuple> searchCond(DibsSearchCond cond, Pageable page);
}
