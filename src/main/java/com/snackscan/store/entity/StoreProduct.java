package com.snackscan.store.entity;

import com.snackscan.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_product_id")
  private Long id;

  @Column(nullable = false)
  private int minStock; // 최소 재고

  @Column(nullable = false)
  private int currentStock; // 현재 재고

  @Column(nullable = false)
  private int storePrice; // 매장 가격

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  // == 비즈니스 메서드 ==//
  public static StoreProduct createStoreProduct(int minStock, int currentStock, int storePrice, Product product,
      Store store) {
    StoreProduct storeProduct = new StoreProduct();
    storeProduct.minStock = minStock;
    storeProduct.currentStock = currentStock;
    storeProduct.storePrice = storePrice;
    storeProduct.product = product;
    storeProduct.store = store;
    return storeProduct;
  }

}
