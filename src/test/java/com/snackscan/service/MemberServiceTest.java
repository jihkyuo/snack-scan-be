package com.snackscan.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.entity.Member;
import com.snackscan.repository.MemberRepository;

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
    Member member = new Member("jio", "지오현", "01012345678");

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
    Member member1 = new Member("jio", "지오현", "01012345678");
    Member member2 = new Member("jio", "지오현", "01012345678");

    // when
    memberService.join(member1);

    // then
    assertThatThrownBy(() -> memberService.join(member2))
        .isInstanceOf(IllegalStateException.class);
  }
}
