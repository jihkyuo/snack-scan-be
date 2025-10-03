package com.snackscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByPhoneNumber(String phoneNumber);

  Member findByLoginId(String loginId);
}
