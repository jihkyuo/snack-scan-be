package com.snackscan.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long id;

  private String name; // 상품 이름

  private String brand; // 브랜드

  private int productPrice; // 상품 가격

  public static Product createProduct(String name, String brand, int productPrice) {
    Product product = new Product();
    product.name = name;
    product.brand = brand;
    product.productPrice = productPrice;
    return product;
  }
}
