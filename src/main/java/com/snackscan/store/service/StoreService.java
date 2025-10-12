package com.snackscan.store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.store.entity.Store;
import com.snackscan.store.exception.StoreErrorCode;
import com.snackscan.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  // 매장 등록
  public Long addStore(Store store) {
    storeRepository.save(store);
    return store.getId();
  }

  // 매장 전체 조회
  public List<Store> findAllStores() {
    return storeRepository.findAll();
  }

  // 매장 삭제
  public void deleteStore(Long storeId) {
    Store store = findOne(storeId);
    storeRepository.delete(store);
  }

  // 매장 단일 조회
  public Store findOne(Long storeId) {
    return findByIdOrThrow(storeId);
  }

  // 매장 ID로 조회, 없으면 예외 발생
  public Store findByIdOrThrow(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));
  }
}
