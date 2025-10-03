package com.snackscan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.entity.Member;
import com.snackscan.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  // 가입
  public Long join(Member member) {
    validateDuplicateMember(member);
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    Member findMember = memberRepository.findByLoginId(member.getLoginId());
    if (findMember != null) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

}
