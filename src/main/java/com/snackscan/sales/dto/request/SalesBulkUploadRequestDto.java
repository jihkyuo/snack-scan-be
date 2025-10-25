package com.snackscan.sales.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesBulkUploadRequestDto {
  @NotNull(message = "매장 ID는 필수입니다")
  private Long storeId;

  @NotNull(message = "매출 목록은 필수입니다")
  private List<SalesItemDto> salesList;
}
