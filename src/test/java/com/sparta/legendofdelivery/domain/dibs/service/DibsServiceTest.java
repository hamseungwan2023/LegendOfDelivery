package com.sparta.legendofdelivery.domain.dibs.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.legendofdelivery.domain.dibs.dto.DibsSearchCond;
import com.sparta.legendofdelivery.domain.dibs.entity.DibsRank;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsRankMapper;
import com.sparta.legendofdelivery.domain.dibs.repository.DibsRepository;
import com.sparta.legendofdelivery.domain.store.entity.Category;
import com.sparta.legendofdelivery.domain.store.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DibsServiceTest {
    @Mock
    DibsRepository dibsRepository;

    @InjectMocks
    DibsService dibsService;

    @PersistenceContext
    EntityManager em;
    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void setUp() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("찜 top 10 가게 조회")
    void top10() {
        // given
        List<DibsRankMapper> mockDibsRank = new ArrayList<>();
        mockDibsRank.add(new DibsRank(10, "Store 1"));
        mockDibsRank.add(new DibsRank(9, "Store 2"));

        when(dibsRepository.getDibsRanks()).thenReturn(mockDibsRank);

        // when
        List<DibsRankMapper> result = dibsService.getDibsRank();

        // then
        assertNotNull(result);
        assertEquals(10, result.get(0).getCount());
        assertEquals("Store 1", result.get(0).getStoreName());
        assertEquals(9, result.get(1).getCount());
        assertEquals("Store 2", result.get(1).getStoreName());
    }

    @Test
    @DisplayName("가게 검색 필터")
    void searchCondFilter () {
        //given
        Store store = new Store();
        store.setCategory(Category.KOREAN);
        store.setDibsCount(15L);

        DibsSearchCond searchCond = DibsSearchCond.builder()
                .maxDibsCount(20L)
                .minDibsCount(10L)
                .category(Category.KOREAN)
                .build();

        //when
        List<Tuple> testSearch = dibsService.searchDibs(searchCond,0);

        //then
        assertNotNull(testSearch);
        assertEquals(0, testSearch.size());
    }

    @Test
    void addDibs() {
    }

    @Test
    void deleteDibs() {
    }

    @Test
    void getAllDibsByUser() {
    }
}