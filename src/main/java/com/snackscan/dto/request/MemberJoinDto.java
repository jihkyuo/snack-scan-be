package com.snackscan.dto.request;

import lombok.Data;

@Data
public class MemberJoinDto {
  private String loginId;
  private String name;
  private String phoneNumber;
}
