package com.snackscan.store.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddStoreEmployeeDto {

  @NotEmpty(message = "직원 ID 목록은 필수입니다")
  private List<Long> memberIds;
}
