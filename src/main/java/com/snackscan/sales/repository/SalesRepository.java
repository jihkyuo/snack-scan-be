package com.snackscan.sales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.sales.entity.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {

  List<Sales> findByStoreId(Long storeId);
}
