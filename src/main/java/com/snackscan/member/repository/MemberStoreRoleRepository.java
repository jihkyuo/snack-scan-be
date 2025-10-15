package com.snackscan.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snackscan.member.entity.MemberStoreRole;
import com.snackscan.member.entity.Role;

public interface MemberStoreRoleRepository extends JpaRepository<MemberStoreRole, Long> {

  // 특정 멤버의 모든 스토어 소속 조회
  List<MemberStoreRole> findByMemberId(Long memberId);

  // 특정 스토어의 모든 멤버 소속 조회
  List<MemberStoreRole> findByStoreId(Long storeId);

  // 특정 멤버와 스토어의 소속 관계 조회
  Optional<MemberStoreRole> findByMemberIdAndStoreId(Long memberId, Long storeId);

  // 멤버가 소속된 스토어 수 조회
  @Query("SELECT COUNT(msr) FROM MemberStoreRole msr WHERE msr.member.id = :memberId")
  long countStoresByMemberId(@Param("memberId") Long memberId);

  // 스토어에 소속된 멤버 수 조회
  @Query("SELECT COUNT(msr) FROM MemberStoreRole msr WHERE msr.store.id = :storeId")
  long countMembersByStoreId(@Param("storeId") Long storeId);

  // 멤버가 특정 스토어에 소속되어 있는지 확인
  boolean existsByMemberIdAndStoreId(Long memberId, Long storeId);

  // 특정 스토어 역할의 멤버들이 소속된 스토어 조회
  @Query("SELECT msr FROM MemberStoreRole msr WHERE msr.storeRole = :role")
  List<MemberStoreRole> findByStoreRole(@Param("role") Role role);

  // 특정 스토어의 특정 역할 멤버들 조회
  List<MemberStoreRole> findByStoreIdAndStoreRole(Long storeId, Role storeRole);

  // 여러 멤버가 특정 스토어에 소속되어 있는지 확인 (성능 최적화)
  @Query("SELECT COUNT(msr) > 0 FROM MemberStoreRole msr WHERE msr.member.id IN :memberIds AND msr.store.id = :storeId")
  boolean hasExistingMembers(@Param("memberIds") List<Long> memberIds, @Param("storeId") Long storeId);
}
