package com.snackscan.member.exception;

import com.snackscan.common.exception.BusinessException;

public class DuplicateMemberException extends BusinessException {
    public DuplicateMemberException() {
        super(MemberErrorCode.DUPLICATE_MEMBER);
    }
    
    public DuplicateMemberException(String message) {
        super(MemberErrorCode.DUPLICATE_MEMBER, message);
    }
}
