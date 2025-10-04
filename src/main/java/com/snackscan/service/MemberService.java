package com.snackscan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.dto.request.MemberUpdateDto;
import com.snackscan.entity.Member;
import com.snackscan.exception.DuplicateMemberException;
import com.snackscan.exception.MemberNotFoundException;
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

  // 회원 조회 (단일)
  public Member findOne(Long memberId) {
    Optional<Member> member = memberRepository.findById(memberId);
    if (member.isEmpty()) {
      throw new MemberNotFoundException("존재하지 않는 회원입니다.");
    }
    return member.get();
  }

  // 전체 회원 조회
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  // 회원 정보 수정
  public void update(Long memberId, MemberUpdateDto updateDto) {
    Member member = findByIdOrThrow(memberId);
    member.updateInfo(updateDto.getName(), updateDto.getPhoneNumber());
  }

  // 회원 삭제
  public void delete(Long memberId) {
    Member member = findByIdOrThrow(memberId);
    memberRepository.delete(member);
  }

  // 중복 회원 검증
  private void validateDuplicateMember(Member member) {
    Member findMember = memberRepository.findByLoginId(member.getLoginId());
    if (findMember != null) {
      throw new DuplicateMemberException("이미 존재하는 회원입니다.");
    }
  }

  // 회원 ID로 조회, 없으면 예외 발생
  private Member findByIdOrThrow(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
  }
}
