package com.snackscan.member.exception;

import org.springframework.http.HttpStatus;

import com.snackscan.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    // Member 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "존재하지 않는 회원입니다."),
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, "MEMBER_002", "이미 존재하는 회원입니다.");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
