package com.snackscan.member.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberUpdateDto {
  @Size(min = 1, max = 50, message = "이름은 1-50자 사이여야 합니다")
  private String name;
  
  @Size(min = 11, max = 13, message = "전화번호는 11-13자 사이여야 합니다")
  private String phoneNumber;
}
