package com.snackscan.store.dto.response;

import com.snackscan.member.entity.MemberStoreRole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreEmployeeResponseDto {
  private Long memberId;
  private String name;
  private String phoneNumber;

  public StoreEmployeeResponseDto(MemberStoreRole memberStoreRole) {
    this.memberId = memberStoreRole.getMember().getId();
    this.name = memberStoreRole.getMember().getName();
    this.phoneNumber = memberStoreRole.getMember().getPhoneNumber();
  }
}