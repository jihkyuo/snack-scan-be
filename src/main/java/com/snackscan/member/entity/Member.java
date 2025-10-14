package com.snackscan.member.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

  @Column(nullable = false, length = 20)
  private String name;

  @Column(nullable = false, length = 20)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<MemberStoreRole> memberStoreRoles = new ArrayList<>();

  public Member(String loginId, String name, String phoneNumber, Role role) {
    this.loginId = loginId;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.role = role;
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

  // 역할 관련 메서드들
  public boolean isOwner() {
    return this.role == Role.OWNER;
  }

  public boolean isEmployee() {
    return this.role == Role.EMPLOYEE;
  }

  public void changeRole(Role newRole) {
    this.role = newRole;
  }

  // 스토어 관련 메서드들
  public void addStoreRole(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.add(memberStoreRole);
  }

}
