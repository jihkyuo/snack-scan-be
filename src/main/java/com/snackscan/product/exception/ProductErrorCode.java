package com.snackscan.product.exception;

import org.springframework.http.HttpStatus;

import com.snackscan.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_001", "존재하지 않는 상품입니다."),
  DUPLICATE_PRODUCT(HttpStatus.CONFLICT, "PRODUCT_002", "이미 존재하는 상품입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
