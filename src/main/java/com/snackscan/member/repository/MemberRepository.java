package com.snackscan.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByPhoneNumber(String phoneNumber);

  Member findByLoginId(String loginId);
}
