package com.snackscan.store.dto.response;

import com.snackscan.store.entity.Store;

import lombok.Data;

@Data
public class StoreResponseDto {

  private Long id;
  private String name;
  private String address;

  public StoreResponseDto(Store store) {
    this.id = store.getId();
    this.name = store.getName();
    this.address = store.getAddress();
  }
}
