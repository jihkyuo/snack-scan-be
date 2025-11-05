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

  // 기존 Product 사용 시 (productId가 있으면 이것 사용)
  private Long productId;

  // 새 Product 생성 시 (productId가 없으면 이것들 필수)
  private String productName;

  private String productBrand;

  private Integer productPrice;

  /**
   * productId가 있으면 기존 Product 사용, 없으면 새 Product 생성
   * @return 기존 Product 사용 여부
   */
  public boolean hasProductId() {
    return productId != null;
  }

  /**
   * Product 생성 필드 검증
   * @return Product 생성 필드가 모두 제공되었는지
   */
  public boolean hasProductCreationFields() {
    return productName != null && !productName.isBlank()
        && productBrand != null && !productBrand.isBlank()
        && productPrice != null;
  }

  /**
   * DTO 유효성 검증
   * @return 유효한 입력인지 여부
   */
  public boolean isValid() {
    // productId가 있거나, Product 생성 필드가 모두 있어야 함
    return hasProductId() || hasProductCreationFields();
  }
}
