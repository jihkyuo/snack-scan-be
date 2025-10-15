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
  STORE_OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_OWNER_001", "존재하지 않는 매장 오너입니다."),
  STORE_OWNER_NOT_UNIQUE(HttpStatus.BAD_REQUEST, "STORE_OWNER_002", "매장 오너는 유니크해야 합니다."),
  MEMBER_ALREADY_IN_STORE(HttpStatus.BAD_REQUEST, "STORE_MEMBER_001", "이미 해당 매장에 소속된 직원입니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_MEMBER_002", "존재하지 않는 회원입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
