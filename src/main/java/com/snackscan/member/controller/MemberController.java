package com.snackscan.member.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackscan.member.dto.request.MemberJoinDto;
import com.snackscan.member.dto.request.MemberUpdateDto;
import com.snackscan.member.dto.response.MemberResponseDto;
import com.snackscan.member.entity.Member;
import com.snackscan.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private static final Logger log = LoggerFactory.getLogger(MemberController.class);
  private final MemberService memberService;

  // 회원 가입
  @PostMapping
  public ResponseEntity<Long> join(@Valid @RequestBody MemberJoinDto request) {
    log.info("회원 가입 요청 - loginId: {}, name: {}", request.getLoginId(), request.getName());
    Member member = new Member(request.getLoginId(), request.getName(), request.getPhoneNumber());
    Long memberId = memberService.join(member);
    log.info("회원 가입 완료 - memberId: {}", memberId);
    return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
  }

  // 회원 조회 (단일)
  @GetMapping("/{id}")
  public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long id) {
    log.info("회원 조회 요청 - memberId: {}", id);
    Member member = memberService.findOne(id);
    log.info("회원 조회 완료 - memberId: {}, loginId: {}", id, member.getLoginId());
    return ResponseEntity.ok(new MemberResponseDto(member));
  }

  // 전체 회원 조회
  @GetMapping
  public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
    log.info("전체 회원 조회 요청");
    List<Member> members = memberService.findMembers();
    List<MemberResponseDto> response = members.stream()
        .map(MemberResponseDto::new)
        .toList();
    log.info("전체 회원 조회 완료 - 조회된 회원 수: {}", response.size());
    return ResponseEntity.ok(response);
  }

  // 회원 정보 수정
  @PutMapping("/{id}")
  public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id, @RequestBody MemberUpdateDto updateDto) {
    log.info("회원 정보 수정 요청 - memberId: {}, name: {}", id, updateDto.getName());
    memberService.update(id, updateDto);
    Member updatedMember = memberService.findOne(id);
    log.info("회원 정보 수정 완료 - memberId: {}", id);
    return ResponseEntity.ok(new MemberResponseDto(updatedMember));
  }

  // 회원 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
    log.info("회원 삭제 요청 - memberId: {}", id);
    memberService.delete(id);
    log.info("회원 삭제 완료 - memberId: {}", id);
    return ResponseEntity.noContent().build();
  }
}
