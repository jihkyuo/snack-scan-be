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

  // 정적 팩토리 메서드
  public static Store createStore(String name, String address) {
    return new Store(name, address);
  }

  // 스토어 관련 메서드들
  public void addMember(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.add(memberStoreRole);
  }

  public void removeMember(MemberStoreRole memberStoreRole) {
    this.memberStoreRoles.remove(memberStoreRole);
  }

  // 스토어 소유자 조회 (사장 역할인 멤버들)
  public List<MemberStoreRole> getOwners() {
    return memberStoreRoles.stream()
        .filter(role -> role.getMember().isOwner())
        .toList();
  }

  // 스토어 직원 조회 (직원 역할인 멤버들)
  public List<MemberStoreRole> getEmployees() {
    return memberStoreRoles.stream()
        .filter(role -> role.getMember().isEmployee())
        .toList();
  }

  // 특정 멤버의 소속 여부 확인
  public boolean hasMember(Long memberId) {
    return memberStoreRoles.stream()
        .anyMatch(role -> role.getMember().getId().equals(memberId));
  }

  // 소속된 멤버 목록 조회
  public List<MemberStoreRole> getMemberStoreRoles() {
    return new ArrayList<>(memberStoreRoles);
  }
}
