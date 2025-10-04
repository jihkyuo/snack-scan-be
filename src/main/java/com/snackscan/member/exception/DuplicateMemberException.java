package com.snackscan.member.exception;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.common.exception.ErrorCode;

public class DuplicateMemberException extends BusinessException {
    public DuplicateMemberException() {
        super(ErrorCode.DUPLICATE_MEMBER);
    }
    
    public DuplicateMemberException(String message) {
        super(ErrorCode.DUPLICATE_MEMBER, message);
    }
}
