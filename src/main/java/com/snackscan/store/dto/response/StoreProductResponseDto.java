package com.snackscan.store.dto.response;

import com.snackscan.product.entity.Product;
import com.snackscan.store.entity.Store;
import com.snackscan.store.entity.StoreProduct;

public class StoreProductResponseDto {

  private Long id;
  private int minStock;
  private int currentStock;
  private int storePrice;
  private Product product;
  private Store store;

  public StoreProductResponseDto(StoreProduct storeProduct) {
    this.id = storeProduct.getId();
    this.minStock = storeProduct.getMinStock();
    this.currentStock = storeProduct.getCurrentStock();
    this.storePrice = storeProduct.getStorePrice();
    this.product = storeProduct.getProduct();
    this.store = storeProduct.getStore();
  }
}
