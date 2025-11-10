package com.snackscan.product.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.snackscan.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Product findByName(String name);

  @Query("SELECT p FROM Product p WHERE p.name IN :productNames")
  List<Product> findAllByNameIn(Set<String> productNames);
}
