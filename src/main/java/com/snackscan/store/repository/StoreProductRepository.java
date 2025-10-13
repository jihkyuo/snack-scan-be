package com.snackscan.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.store.entity.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

  List<StoreProduct> findByStoreId(Long storeId);
}
