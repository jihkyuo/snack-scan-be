package com.snackscan.sales.dto.response;

import java.time.LocalDateTime;

import com.snackscan.sales.entity.Sales;

import lombok.Getter;

@Getter
public class SalesResponseDto {
  private Long id;
  private Long storeId;
  private Long productId;
  private String productName;
  private int quantity;
  private int unitPrice;
  private int totalAmount;
  private LocalDateTime saleDate;

  public SalesResponseDto(Sales sales) {
    this.id = sales.getId();
    this.storeId = sales.getStore().getId();
    this.productId = sales.getProduct().getId();
    this.productName = sales.getProduct().getName();
    this.quantity = sales.getQuantity();
    this.unitPrice = sales.getUnitPrice();
    this.totalAmount = sales.getTotalAmount();
    this.saleDate = sales.getSaleDate();
  }
}
