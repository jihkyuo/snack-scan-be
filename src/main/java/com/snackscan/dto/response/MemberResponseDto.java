package com.snackscan.dto.response;

import lombok.Data;
import com.snackscan.entity.Member;

@Data
public class MemberResponseDto {
  private Long id;
  private String loginId;
  private String name;
  private String phoneNumber;

  public MemberResponseDto(Member member) {
    this.id = member.getId();
    this.loginId = member.getLoginId();
    this.name = member.getName();
    this.phoneNumber = member.getPhoneNumber();
  }
}
