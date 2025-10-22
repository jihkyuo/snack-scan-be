package com.snackscan.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.sales.entity.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {

}
