package com.snackscan.member.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<MemberStoreRole> memberStoreRoles = new ArrayList<>();

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

  // 스토어 관련 메서드들
  public void addStoreRole(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.add(memberStoreRole);
  }

  public void removeStoreRole(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.remove(memberStoreRole);
  }

  // 특정 스토어에서의 역할 조회
  public Role getRoleInStore(Long storeId) {
    return memberStoreRoles.stream()
        .filter(role -> role.getStore().getId().equals(storeId))
        .map(MemberStoreRole::getRole)
        .findFirst()
        .orElse(null);
  }

  // 소유한 스토어 목록 조회
  public List<MemberStoreRole> getOwnedStores() {
    return memberStoreRoles.stream()
        .filter(MemberStoreRole::isOwner)
        .toList();
  }

  // 근무하는 스토어 목록 조회
  public List<MemberStoreRole> getWorkingStores() {
    return memberStoreRoles.stream()
        .filter(MemberStoreRole::isEmployee)
        .toList();
  }
}
