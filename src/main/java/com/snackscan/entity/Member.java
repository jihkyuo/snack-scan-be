package com.snackscan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
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

}
