package com.snackscan.member.dto.response;

import com.snackscan.member.entity.Member;

import lombok.Data;

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
