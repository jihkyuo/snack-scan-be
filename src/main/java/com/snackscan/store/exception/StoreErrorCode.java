package com.snackscan.store.exception;

import org.springframework.http.HttpStatus;

import com.snackscan.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
  STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_001", "존재하지 않는 매장입니다."),
  STORE_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_PRODUCT_001", "존재하지 않는 매장 상품입니다."),
  STORE_OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_OWNER_001", "존재하지 않는 매장 오너입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
