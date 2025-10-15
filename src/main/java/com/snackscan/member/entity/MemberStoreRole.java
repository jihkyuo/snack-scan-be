package com.snackscan.member.entity;

import com.snackscan.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "store_id"})
})
public class MemberStoreRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_store_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role storeRole; // 스토어별 역할 (OWNER, EMPLOYEE)

    // 생성자
    private MemberStoreRole(Member member, Store store, Role storeRole) {
        this.member = member;
        this.store = store;
        this.storeRole = storeRole;
    }

    // 정적 팩토리 메서드
    public static MemberStoreRole createMemberStoreRelation(Member member, Store store, Role storeRole) {
        MemberStoreRole memberStoreRole = new MemberStoreRole(member, store, storeRole);
        member.addMemberStoreRole(memberStoreRole);
        store.addMember(memberStoreRole);
        return memberStoreRole;
    }

    // 역할 변경 메서드
    public void changeStoreRole(Role newStoreRole) {
        this.storeRole = newStoreRole;
    }

    // 역할 확인 메서드들
    public boolean isOwner() {
        return this.storeRole == Role.OWNER;
    }

    public boolean isEmployee() {
        return this.storeRole == Role.EMPLOYEE;
    }
}
