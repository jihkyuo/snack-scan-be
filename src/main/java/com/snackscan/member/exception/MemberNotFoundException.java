package com.snackscan.member.exception;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.common.exception.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
    
    public MemberNotFoundException(String message) {
        super(ErrorCode.MEMBER_NOT_FOUND, message);
    }
}
