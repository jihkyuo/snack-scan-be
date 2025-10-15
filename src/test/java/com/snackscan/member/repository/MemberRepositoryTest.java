package com.snackscan.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.member.entity.Member;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  public void findByPhoneNumber() {
    Member member = new Member("test1234", "John Doe", "01099999999");
    Member savedMember = memberRepository.save(member);
    Member foundMember = memberRepository.findByPhoneNumber("01099999999");

    assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    assertThat(foundMember.getName()).isEqualTo(savedMember.getName());
    assertThat(foundMember).isEqualTo(savedMember);
  }
}
