package com.snackscan.store.entity;

import java.util.ArrayList;
import java.util.List;

import com.snackscan.member.entity.MemberStoreRole;

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
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_id")
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 100)
  private String address;

  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
  private List<MemberStoreRole> memberStoreRoles = new ArrayList<>();

  public Store(String name, String address) {
    this.name = name;
    this.address = address;
  }

  // 스토어 관련 메서드들
  public void addMemberRole(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.add(memberStoreRole);
  }

  public void removeMemberRole(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.remove(memberStoreRole);
  }

  // 스토어 소유자 조회
  public List<MemberStoreRole> getOwners() {
    return memberStoreRoles.stream()
        .filter(MemberStoreRole::isOwner)
        .toList();
  }

  // 스토어 직원 조회
  public List<MemberStoreRole> getEmployees() {
    return memberStoreRoles.stream()
        .filter(MemberStoreRole::isEmployee)
        .toList();
  }

  // 특정 멤버의 역할 조회
  public MemberStoreRole getMemberRole(Long memberId) {
    return memberStoreRoles.stream()
        .filter(role -> role.getMember().getId().equals(memberId))
        .findFirst()
        .orElse(null);
  }
}
