package com.snackscan.supplier.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EditSupplierRequestDto {

  @Size(min = 1, max = 100, message = "공급자 이름은 1-100자 사이여야 합니다")
  private String name;

  @Size(min = 1, max = 100, message = "공급자 주소는 1-100자 사이여야 합니다")
  private String address;

  @Size(min = 11, max = 13, message = "공급자 전화번호는 11-13자 사이여야 합니다")
  private String phoneNumber;

  @Email(message = "공급자 이메일은 이메일 형식이어야 합니다")
  @Size(min = 1, max = 100, message = "공급자 이메일은 1-100자 사이여야 합니다")
  private String email;

  @Size(min = 1, max = 100, message = "공급자 웹사이트는 1-100자 사이여야 합니다")
  private String website;
}
