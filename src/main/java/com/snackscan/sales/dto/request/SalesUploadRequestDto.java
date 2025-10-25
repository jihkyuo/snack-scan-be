package com.snackscan.sales.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SalesUploadRequestDto extends SalesItemDto {
  @NotNull(message = "매장 ID는 필수입니다")
  private Long storeId;
}
