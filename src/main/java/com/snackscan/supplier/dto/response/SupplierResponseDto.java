package com.snackscan.supplier.dto.response;

import com.snackscan.supplier.entity.Supplier;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SupplierResponseDto {
  private Long id;
  private String name;
  private String address;
  private String phoneNumber;
  private String email;
  private String website;

  public static SupplierResponseDto from(Supplier supplier) {
    return new SupplierResponseDto(
        supplier.getId(),
        supplier.getName(),
        supplier.getAddress(),
        supplier.getPhoneNumber(),
        supplier.getEmail(),
        supplier.getWebsite());
  }
}
