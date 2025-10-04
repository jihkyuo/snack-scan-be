package com.snackscan.exception;

public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException(String message) {
        super(message);
    }
    
    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
