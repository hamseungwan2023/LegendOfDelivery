package com.sparta.legendofdelivery.domain.review.entity;

import com.sparta.legendofdelivery.domain.review.dto.CreateReviewRequestDto;
import com.sparta.legendofdelivery.domain.review.dto.UpdateReviewRequestDto;
import com.sparta.legendofdelivery.domain.store.entity.Store;
import com.sparta.legendofdelivery.domain.user.entity.User;
import com.sparta.legendofdelivery.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  @Column(name = "like_count")
  private Long likeCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

//  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<Like> likeList = new ArrayList<>();

  public Review(CreateReviewRequestDto requestDto,Store store, User user) {
    this.content = requestDto.getComment();
    this.store = store;
    this.user = user;
    this.likeCount = 0L;
  }

  public void updateReview(UpdateReviewRequestDto requestDto,Store store, User user) {
    this.content = requestDto.getContent();
    this.store = store;
    this.user = user;
  }

  public void upLikeCount () {
    this.likeCount += 1L;
  }

  public void downLikeCount () {
    this.likeCount += 1L;
  }

}
