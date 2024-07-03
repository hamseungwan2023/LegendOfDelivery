package com.sparta.legendofdelivery.domain.dibs.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.legendofdelivery.domain.dibs.dto.DibsSearchCond;
import com.sparta.legendofdelivery.domain.dibs.entity.QDibs;
import com.sparta.legendofdelivery.domain.store.entity.QStore;
import com.sparta.legendofdelivery.domain.user.entity.QUser;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class DibsRepositoryImpl implements DibsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DibsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Tuple> searchCond(DibsSearchCond cond, Pageable page) {
        QDibs dibs = QDibs.dibs;
        QStore store = QStore.store;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (cond.getCategory() != null) {
            builder.and(dibs.store.category.eq(cond.getCategory()));
        }
        if (cond.getMinDibsCount() != null) {
            builder.and(store.dibsCount.goe(cond.getMinDibsCount()));
        }
        if (cond.getMaxDibsCount() != null) {
            builder.and(store.dibsCount.loe(cond.getMaxDibsCount()));
        }

        return queryFactory.select(store.name, user.name)
                .from(dibs)
                .join(store).on(store.id.eq(dibs.store.id))
                .join(user).on(user.id.eq(dibs.user.id))
                .where(builder)
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

    }
}
