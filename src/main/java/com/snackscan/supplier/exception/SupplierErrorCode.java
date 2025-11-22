package com.snackscan.supplier.exception;

import org.springframework.http.HttpStatus;

import com.snackscan.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupplierErrorCode implements ErrorCode {
  SUPPLIER_NOT_FOUND(HttpStatus.NOT_FOUND, "SUPPLIER_001", "존재하지 않는 공급자입니다."),
  DUPLICATE_SUPPLIER(HttpStatus.CONFLICT, "SUPPLIER_002", "이미 존재하는 공급자입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
