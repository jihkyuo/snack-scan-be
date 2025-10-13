package com.snackscan.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Product findByName(String name);
}
