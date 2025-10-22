package com.snackscan.sales.entity;

import java.time.LocalDateTime;

import com.snackscan.product.entity.Product;
import com.snackscan.store.entity.Store;

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
public class Sales {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sales_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private int unitPrice; // 단가

  @Column(nullable = false)
  private int totalAmount; // 총 가격

  @Column(nullable = false)
  private LocalDateTime saleDate; // 판매 일시

  // == 비즈니스 메서드 ==//
  public static Sales createSales(Store store, Product product, int quantity, int unitPrice) {
    Sales sales = new Sales();
    sales.store = store;
    sales.product = product;
    sales.quantity = quantity;
    sales.unitPrice = unitPrice;
    sales.totalAmount = quantity * unitPrice;
    sales.saleDate = LocalDateTime.now();
    return sales;
  }
}
