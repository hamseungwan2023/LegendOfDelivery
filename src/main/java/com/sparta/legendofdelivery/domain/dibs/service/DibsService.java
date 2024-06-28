package com.sparta.legendofdelivery.domain.dibs.service;

import com.sparta.legendofdelivery.domain.dibs.dto.DibsResponseDto;
import com.sparta.legendofdelivery.domain.dibs.entity.Dibs;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsPageMapper;
import com.sparta.legendofdelivery.domain.dibs.repository.DibsRepository;
import com.sparta.legendofdelivery.domain.store.entity.Store;
import com.sparta.legendofdelivery.domain.store.repository.StoreRepository;
import com.sparta.legendofdelivery.domain.user.entity.User;
import com.sparta.legendofdelivery.global.dto.DataResponse;
import com.sparta.legendofdelivery.global.dto.MessageResponse;
import com.sparta.legendofdelivery.global.exception.BadRequestException;
import com.sparta.legendofdelivery.global.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DibsService {

    private static final Logger log = LoggerFactory.getLogger(DibsService.class);
    private final DibsRepository dibsRepository;
    private final StoreRepository storeRepository;

    public DibsService(DibsRepository dibsRepository, StoreRepository storeRepository) {
        this.dibsRepository = dibsRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public MessageResponse addDibs(Long storeId, User user){

        Store store = findStoreById(storeId);
        Dibs checkIsDibs = findDibsByStoreIdAndUserId(storeId, user.getId());

        if (checkIsDibs == null){
            Dibs dibs = new Dibs(user, store);
            dibsRepository.save(dibs);
            store.upDibsCount();
            return new MessageResponse(200, "가게 찜에 성공했습니다.");
        } else {
            throw new BadRequestException("이미 찜한 가게입니다.");
        }

    }

    @Transactional
    public MessageResponse deleteDibs(Long storeId, User user){

        Store store = findStoreById(storeId);
        Dibs checkIsDibs = findDibsByStoreIdAndUserId(storeId, user.getId());

        if (checkIsDibs != null){
            dibsRepository.delete(checkIsDibs);
            store.downDibsCount();
            return new MessageResponse(200, "찜 삭제를 성공했습니다.");
        } else {
            throw new NotFoundException("해당 가게는 찜한 가게가 아닙니다.");
        }

    }

    public Page<DibsPageMapper> getAllDibsByUser(User user, int page){

        Pageable pageable = PageRequest.of(page,5);
        Page<DibsPageMapper> dibsList = dibsRepository.dibsPaging(user,pageable);

        if (!dibsList.isEmpty()){
            return dibsList;
        } else {
            throw new NotFoundException("찜한 가게가 없습니다.");
        }

    }

    private Store findStoreById(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(
                () -> new NotFoundException("해당 id를 가진 가게가 없습니다.")
        );
    }

    private Dibs findDibsByStoreIdAndUserId(Long storeId, Long userId){
        return dibsRepository.findByStoreIdAndUserId(storeId, userId).orElse(null);
    }

}
