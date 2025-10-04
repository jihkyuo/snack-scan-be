package com.snackscan.member.exception;

import com.snackscan.common.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
    
    public MemberNotFoundException(String message) {
        super(MemberErrorCode.MEMBER_NOT_FOUND, message);
    }
}
