package com.snackscan.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddStoreDto {
  @NotBlank(message = "매장 이름은 필수입니다")
  private String name;
  
  @NotBlank(message = "매장 주소는 필수입니다")
  private String address;

}
