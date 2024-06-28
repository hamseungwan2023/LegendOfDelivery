package com.sparta.legendofdelivery.domain.dibs.repository;

import com.sparta.legendofdelivery.domain.dibs.entity.Dibs;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsPageMapper;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsRankMapper;
import com.sparta.legendofdelivery.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DibsRepository extends JpaRepository<Dibs, Long> {
    Optional<Dibs> findByStoreIdAndUserId(long storeId, long userId);

    @Query(value =
            "SELECT user.name as user_name, store.name as store_name " +
                    "FROM dibs d " +
                    "JOIN store ON store.id = d.store_id " +
                    "JOIN user ON user.id = d.user_id " +
                    "order by d.create_at desc",
            nativeQuery = true)
    Page<DibsPageMapper> dibsPaging(User user, Pageable pageable);

    @Query(value =
            "SELECT COUNT(*) AS count, s.name AS store_name " +
                    "FROM dibs d " +
                    "JOIN store s ON d.store_id = s.id " +
                    "GROUP BY d.store_id " +
                    "LIMIT 10",
            nativeQuery = true)
    List<DibsRankMapper> getDibsRanks();
}
