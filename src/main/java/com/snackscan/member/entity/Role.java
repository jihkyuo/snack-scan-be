package com.snackscan.member.entity;

public enum Role {
    OWNER("사장"),
    EMPLOYEE("직원");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
