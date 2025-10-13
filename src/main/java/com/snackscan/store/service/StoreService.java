package com.snackscan.store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.store.entity.Store;
import com.snackscan.store.entity.StoreProduct;
import com.snackscan.store.exception.StoreErrorCode;
import com.snackscan.store.repository.StoreProductRepository;
import com.snackscan.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final StoreProductRepository storeProductRepository;
  private final ProductService productService;

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
    Store store = findStoreByIdOrThrow(storeId);
    storeRepository.delete(store);
  }

  // 매장 ID로 조회, 없으면 예외 발생
  public Store findStoreByIdOrThrow(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));
  }

  // 매장 상품 조회
  public List<StoreProduct> findStoreProducts(Long storeId) {
    return storeProductRepository.findByStoreId(storeId);
  }

  // 매장 상품 등록 (Product 자동 생성 포함)
  public Long addStoreProduct(Long storeId, Long productId, int minStock, int currentStock,
      int storePrice) {
    // Store 조회
    Store store = findStoreByIdOrThrow(storeId);

    // Product 처리
    Product product = productService.findProductByIdOrThrow(productId);

    // StoreProduct 생성 및 저장
    StoreProduct storeProduct = StoreProduct.createStoreProduct(minStock, currentStock, storePrice, product, store);

    storeProductRepository.save(storeProduct);
    return storeProduct.getId();
  }

  // 매장 상품 삭제
  public void deleteStoreProduct(Long storeProductId) {
    StoreProduct storeProduct = findStoreProductByIdOrThrow(storeProductId);
    storeProductRepository.delete(storeProduct);
  }

  // 매장 상품 단일 조회
  public StoreProduct findStoreProductByIdOrThrow(Long storeProductId) {
    return storeProductRepository.findById(storeProductId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_PRODUCT_NOT_FOUND));
  }
}
