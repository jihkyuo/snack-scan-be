package com.snackscan.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductRequestDto {
  @NotBlank(message = "상품 이름은 필수입니다")
  private String name;

  @NotBlank(message = "상품 브랜드는 필수입니다")
  private String brand;

  @NotNull(message = "상품 가격은 필수입니다")
  private int productPrice;

}
