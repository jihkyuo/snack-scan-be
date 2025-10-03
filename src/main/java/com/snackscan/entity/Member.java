package com.snackscan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, length = 20)
  private String loginId;

  private String name;

  @Column(nullable = false, length = 20)
  private String phoneNumber;

  public Member(String loginId, String name, String phoneNumber) {
    this.loginId = loginId;
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  // 정보 수정 메서드
  public void updateInfo(String name, String phoneNumber) {
    if (name != null && !name.trim().isEmpty()) {
      this.name = name;
    }
    if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
      this.phoneNumber = phoneNumber;
    }
  }

}
