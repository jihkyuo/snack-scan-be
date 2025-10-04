package com.snackscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberJoinDto {
  @NotBlank(message = "로그인 ID는 필수입니다")
  @Size(min = 3, max = 20, message = "로그인 ID는 3-20자 사이여야 합니다")
  private String loginId;
  
  @NotBlank(message = "이름은 필수입니다")
  private String name;
  
  @NotBlank(message = "전화번호는 필수입니다")
  @Size(min = 11, max = 13, message = "전화번호는 11-13자 사이여야 합니다")
  private String phoneNumber;
}
