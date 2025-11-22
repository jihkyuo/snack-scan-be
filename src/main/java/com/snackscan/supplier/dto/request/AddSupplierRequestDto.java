package com.snackscan.supplier.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddSupplierRequestDto {
  @NotBlank(message = "공급자 이름은 필수입니다")
  private String name;

  private String address;

  @NotBlank(message = "공급자 전화번호는 필수입니다")
  private String phoneNumber;

  @NotBlank(message = "공급자 이메일은 필수입니다")
  @Email(message = "공급자 이메일은 이메일 형식이어야 합니다")
  private String email;

  private String website;

}
