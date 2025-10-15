package com.snackscan.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.member.dto.request.MemberUpdateDto;
import com.snackscan.member.entity.Member;
import com.snackscan.member.exception.MemberErrorCode;
import com.snackscan.member.repository.MemberRepository;

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
    return findByIdOrThrow(memberId);
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
      throw new BusinessException(MemberErrorCode.DUPLICATE_MEMBER);
    }
  }

  // 회원 ID로 조회, 없으면 예외 발생
  public Member findByIdOrThrow(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
  }

  // 여러 회원 ID로 조회
  public List<Member> findByIds(List<Long> memberIds) {
    return memberRepository.findAllById(memberIds);
  }
}
