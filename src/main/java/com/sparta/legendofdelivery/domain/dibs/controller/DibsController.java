package com.sparta.legendofdelivery.domain.dibs.controller;

import com.querydsl.core.Tuple;
import com.sparta.legendofdelivery.domain.dibs.dto.DibsSearchCond;
import com.sparta.legendofdelivery.domain.dibs.dto.StoreUserDto;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsPageMapper;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsRankMapper;
import com.sparta.legendofdelivery.domain.dibs.service.DibsService;
import com.sparta.legendofdelivery.domain.store.entity.Category;
import com.sparta.legendofdelivery.domain.store.entity.QStore;
import com.sparta.legendofdelivery.domain.user.entity.QUser;
import com.sparta.legendofdelivery.domain.user.security.UserDetailsImpl;
import com.sparta.legendofdelivery.global.dto.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DibsController {

    private final DibsService dibsService;

    public DibsController(DibsService dibsService) {
        this.dibsService = dibsService;
    }

    @PostMapping("/stores/{storeId}/dibs")
    public ResponseEntity<MessageResponse> addDibs(@PathVariable Long storeId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(dibsService.addDibs(storeId, userDetails.getUser()));

    }

    @DeleteMapping("/stores/{storeId}/dibs")
    public ResponseEntity<MessageResponse> deleteDibs(@PathVariable Long dibsId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(dibsService.deleteDibs(dibsId, userDetails.getUser()));

    }

    @GetMapping("/dibs/my")
    public ResponseEntity<Page<DibsPageMapper>> getAllDibsByUser(@RequestParam(defaultValue = "0") int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(dibsService.getAllDibsByUser(userDetails.getUser(),page));

    }

    @GetMapping("/stores/dibs/rank")
    public ResponseEntity<List<DibsRankMapper>> getDibsRank() {

        return ResponseEntity.ok(dibsService.getDibsRank());

    }

    @GetMapping("/search")
    public List<StoreUserDto> searchDibs(@RequestParam(required = false) Category category,
                                         @RequestParam(required = false) Long minDibsCount,
                                         @RequestParam(required = false) Long maxDibsCount,
                                         @RequestParam(required = false) int page) {
        DibsSearchCond cond = DibsSearchCond.builder()
                .category(category)
                .minDibsCount(minDibsCount)
                .maxDibsCount(maxDibsCount)
                .build();

        List<Tuple> tuples = dibsService.searchDibs(cond, page);


        return tuples.stream()
                .map(tuple -> new StoreUserDto(
                        tuple.get(QStore.store.name),
                        tuple.get(QUser.user.name)))
                .collect(Collectors.toList());

    }

}
