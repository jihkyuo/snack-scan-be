package com.snackscan.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.member.dto.request.MemberUpdateDto;
import com.snackscan.member.entity.Member;
import com.snackscan.member.entity.Role;
import com.snackscan.member.repository.MemberRepository;

@SpringBootTest
@Transactional
public class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Test
  void 회원가입() {
    // given
    Member member = new Member("jio", "지오현", "01012345678", Role.OWNER);

    // when
    memberService.join(member);

    // then
    Member foundMember = memberRepository.findByLoginId(member.getLoginId());
    assertThat(foundMember.getId()).isEqualTo(member.getId());
    assertThat(foundMember.getName()).isEqualTo(member.getName());
    assertThat(foundMember.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
  }

  @Test
  void 중복_회원_예외() throws Exception {
    // given
    Member member1 = new Member("jio", "지오현", "01012345678", Role.OWNER);
    Member member2 = new Member("jio", "지오현", "01012345678", Role.OWNER);

    // when
    memberService.join(member1);

    // then
    assertThatThrownBy(() -> memberService.join(member2))
        .isInstanceOf(BusinessException.class)
        .hasMessage("이미 존재하는 회원입니다.");
  }

  @Test
  void 회원_정보_수정() {
    // given
    Member member = new Member("jio", "지오현", "01012345678", Role.OWNER);
    memberService.join(member);

    // when
    MemberUpdateDto updateDto = new MemberUpdateDto();
    updateDto.setName("새이름");
    updateDto.setPhoneNumber("01098765432");
    memberService.update(member.getId(), updateDto);
    Member updatedMember = memberService.findOne(member.getId());

    // then
    assertThat(updatedMember.getName()).isEqualTo("새이름");
    assertThat(updatedMember.getPhoneNumber()).isEqualTo("01098765432");
  }

  @Test
  void 회원_정보_부분_수정_이름만() {
    // given
    Member member = new Member("jio", "지오현", "01012345678", Role.OWNER);
    memberService.join(member);

    // when
    MemberUpdateDto updateDto = new MemberUpdateDto();
    updateDto.setName("새이름");
    // phoneNumber는 null로 두어 기존 값 유지
    memberService.update(member.getId(), updateDto);

    // then
    Member updatedMember = memberService.findOne(member.getId());
    assertThat(updatedMember.getName()).isEqualTo("새이름");
    assertThat(updatedMember.getPhoneNumber()).isEqualTo("01012345678"); // 기존 값 유지
  }

  @Test
  void 회원_정보_부분_수정_전화번호만() {
    // given
    Member member = new Member("jio", "지오현", "01012345678", Role.OWNER);
    memberService.join(member);

    // when
    MemberUpdateDto updateDto = new MemberUpdateDto();
    updateDto.setPhoneNumber("01098765432");
    // name은 null로 두어 기존 값 유지
    memberService.update(member.getId(), updateDto);

    // then
    Member updatedMember = memberService.findOne(member.getId());
    assertThat(updatedMember.getName()).isEqualTo("지오현"); // 기존 값 유지
    assertThat(updatedMember.getPhoneNumber()).isEqualTo("01098765432");
  }

  @Test
  void 회원_정보_수정_빈_문자열_무시() {
    // given
    Member member = new Member("jio", "지오현", "01012345678", Role.OWNER);
    memberService.join(member);

    // when
    MemberUpdateDto updateDto = new MemberUpdateDto();
    updateDto.setName(""); // 빈 문자열
    updateDto.setPhoneNumber(" "); // 공백만 있는 문자열
    memberService.update(member.getId(), updateDto);

    // then
    Member updatedMember = memberService.findOne(member.getId());
    assertThat(updatedMember.getName()).isEqualTo("지오현"); // 기존 값 유지
    assertThat(updatedMember.getPhoneNumber()).isEqualTo("01012345678"); // 기존 값
  }

  @Test
  void 존재하지_않는_회원_수정_예외() {
    // given
    Long nonExistentId = 999L;

    // when & then
    assertThatThrownBy(() -> memberService.update(nonExistentId, new MemberUpdateDto()))
        .isInstanceOf(BusinessException.class)
        .hasMessage("존재하지 않는 회원입니다.");
  }
}
