package com.snackscan.sales.exception;

import org.springframework.http.HttpStatus;

import com.snackscan.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalesErrorCode implements ErrorCode {
  SALES_NOT_FOUND(HttpStatus.NOT_FOUND, "SALES_001", "존재하지 않는 매출입니다."),
  STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "SALES_002", "존재하지 않는 매장입니다."),
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "SALES_003", "존재하지 않는 상품입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
