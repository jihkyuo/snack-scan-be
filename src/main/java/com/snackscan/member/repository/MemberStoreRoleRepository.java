package com.snackscan.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snackscan.member.entity.MemberStoreRole;
import com.snackscan.member.entity.Role;

public interface MemberStoreRoleRepository extends JpaRepository<MemberStoreRole, Long> {

    // 특정 멤버의 모든 스토어 역할 조회
    List<MemberStoreRole> findByMemberId(Long memberId);

    // 특정 스토어의 모든 멤버 역할 조회
    List<MemberStoreRole> findByStoreId(Long storeId);

    // 특정 멤버와 스토어의 역할 조회
    Optional<MemberStoreRole> findByMemberIdAndStoreId(Long memberId, Long storeId);

    // 특정 역할의 멤버들 조회
    List<MemberStoreRole> findByRole(Role role);

    // 특정 스토어의 특정 역할 멤버들 조회
    List<MemberStoreRole> findByStoreIdAndRole(Long storeId, Role role);

    // 특정 멤버의 특정 역할 스토어들 조회
    List<MemberStoreRole> findByMemberIdAndRole(Long memberId, Role role);

    // 멤버가 소유한 스토어 수 조회
    @Query("SELECT COUNT(msr) FROM MemberStoreRole msr WHERE msr.member.id = :memberId AND msr.role = 'OWNER'")
    long countOwnedStoresByMemberId(@Param("memberId") Long memberId);

    // 멤버가 근무하는 스토어 수 조회
    @Query("SELECT COUNT(msr) FROM MemberStoreRole msr WHERE msr.member.id = :memberId AND msr.role = 'EMPLOYEE'")
    long countWorkingStoresByMemberId(@Param("memberId") Long memberId);

    // 스토어의 소유자 수 조회
    @Query("SELECT COUNT(msr) FROM MemberStoreRole msr WHERE msr.store.id = :storeId AND msr.role = 'OWNER'")
    long countOwnersByStoreId(@Param("storeId") Long storeId);

    // 스토어의 직원 수 조회
    @Query("SELECT COUNT(msr) FROM MemberStoreRole msr WHERE msr.store.id = :storeId AND msr.role = 'EMPLOYEE'")
    long countEmployeesByStoreId(@Param("storeId") Long storeId);

    // 멤버가 특정 스토어에서 특정 역할을 가지고 있는지 확인
    boolean existsByMemberIdAndStoreIdAndRole(Long memberId, Long storeId, Role role);
}
