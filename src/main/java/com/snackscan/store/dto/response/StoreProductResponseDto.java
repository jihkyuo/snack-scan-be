package com.snackscan.store.dto.response;

import com.snackscan.product.entity.Product;
import com.snackscan.store.entity.StoreProduct;

public class StoreProductResponseDto {

  private Long id;
  private int minStock;
  private int currentStock;
  private int storePrice;
  private Long productId;
  private String productName;
  private String productBrand;
  private int productPrice;

  public StoreProductResponseDto(StoreProduct storeProduct) {
    this.id = storeProduct.getId();
    this.minStock = storeProduct.getMinStock();
    this.currentStock = storeProduct.getCurrentStock();
    this.storePrice = storeProduct.getStorePrice();

    Product product = storeProduct.getProduct();
    this.productId = product.getId();
    this.productName = product.getName();
    this.productBrand = product.getBrand();
    this.productPrice = product.getProductPrice();
  }
}
