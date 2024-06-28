package com.sparta.legendofdelivery.domain.like.repository;

import com.sparta.legendofdelivery.domain.like.entity.Like;
import com.sparta.legendofdelivery.domain.like.mapper.LikePageMapper;
import com.sparta.legendofdelivery.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findLikeByReviewIdAndUserId(Long reviewId, Long userId);

    @Query(value =
            "SELECT user.name as user_name, review.content as review_content " +
            "FROM likes l " +
            "JOIN review ON review.id = l.review_id " +
            "JOIN user ON user.id = l.user_id " +
            "order by l.create_at desc"
            ,nativeQuery = true)
    Page<LikePageMapper> UsersLikePaging(User user,Pageable pageable);
}
