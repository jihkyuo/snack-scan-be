package com.snackscan.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.store.entity.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

  List<StoreProduct> findByStoreId(Long storeId);

  Optional<StoreProduct> findByStoreIdAndProductId(Long storeId, Long productId);

  List<StoreProduct> findByStoreIdAndProductIdIn(Long storeId, List<Long> productIds);
}
