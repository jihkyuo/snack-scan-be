package com.snackscan.sales.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesItemDto {
  @NotNull(message = "상품 이름은 필수입니다")
  private String productName;

  @NotNull(message = "수량은 필수입니다")
  private int quantity;

  @NotNull(message = "단가는 필수입니다")
  private int unitPrice;
}
