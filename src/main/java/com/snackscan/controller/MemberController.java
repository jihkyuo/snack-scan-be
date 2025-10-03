package com.snackscan.controller;

import java.util.List;

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

import com.snackscan.dto.request.MemberJoinDto;
import com.snackscan.dto.request.MemberUpdateDto;
import com.snackscan.dto.response.MemberResponseDto;
import com.snackscan.entity.Member;
import com.snackscan.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  // 회원 가입
  @PostMapping
  public ResponseEntity<Long> join(@RequestBody MemberJoinDto request) {
    try {
      Member member = new Member(request.getLoginId(), request.getName(), request.getPhoneNumber());
      Long memberId = memberService.join(member);
      return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  // 회원 조회 (단일)
  @GetMapping("/{id}")
  public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long id) {
    Member member = memberService.findOne(id);
    if (member == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new MemberResponseDto(member));
  }

  // 전체 회원 조회
  @GetMapping
  public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
    List<Member> members = memberService.findMembers();
    List<MemberResponseDto> response = members.stream()
        .map(MemberResponseDto::new)
        .toList();
    return ResponseEntity.ok(response);
  }

  // 회원 정보 수정
  @PutMapping("/{id}")
  public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id, @RequestBody MemberUpdateDto updateDto) {
    try {
      memberService.update(id, updateDto);
      Member updatedMember = memberService.findOne(id);
      return ResponseEntity.ok(new MemberResponseDto(updatedMember));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // 회원 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
    try {
      memberService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}