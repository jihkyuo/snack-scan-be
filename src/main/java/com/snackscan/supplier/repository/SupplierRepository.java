package com.snackscan.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.supplier.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}