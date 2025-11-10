package com.snackscan.member.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(MemberService.class);
  private final MemberRepository memberRepository;

  // 가입
  public Long join(Member member) {
    log.debug("회원 가입 처리 시작 - loginId: {}", member.getLoginId());
    validateDuplicateMember(member);
    memberRepository.save(member);
    log.debug("회원 가입 처리 완료 - memberId: {}", member.getId());
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
    log.debug("중복 회원 검증 시작 - loginId: {}", member.getLoginId());
    Member findMember = memberRepository.findByLoginId(member.getLoginId());
    if (findMember != null) {
      log.warn("중복 회원 가입 시도 - loginId: {}", member.getLoginId());
      throw new BusinessException(MemberErrorCode.DUPLICATE_MEMBER);
    }
    log.debug("중복 회원 검증 통과 - loginId: {}", member.getLoginId());
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
