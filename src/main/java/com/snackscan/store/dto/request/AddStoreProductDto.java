package com.snackscan.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddStoreProductDto {
  @NotNull(message = "최소 재고는 필수입니다")
  private int minStock;

  @NotNull(message = "현재 재고는 필수입니다")
  private int currentStock;

  @NotNull(message = "매장 가격은 필수입니다")
  private int storePrice;

  @NotNull(message = "상품 ID는 필수입니다")
  private Long productId;

}
