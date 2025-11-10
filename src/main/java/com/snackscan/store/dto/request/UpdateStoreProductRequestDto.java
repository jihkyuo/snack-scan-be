package com.snackscan.store.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateStoreProductRequestDto {

  @PositiveOrZero(message = "최소 재고는 0 이상이어야 합니다")
  private Integer minStock;

  @PositiveOrZero(message = "현재 재고는 0 이상이어야 합니다")
  private Integer currentStock;

  @PositiveOrZero(message = "보충 재고는 0 이상이어야 합니다")
  private Integer supplementStock;

  @PositiveOrZero(message = "매장 가격은 0 이상이어야 합니다")
  private Integer storePrice;
}
