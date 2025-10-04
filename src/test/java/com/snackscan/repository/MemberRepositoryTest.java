package com.snackscan.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.entity.Member;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  public void findByPhoneNumber() {
    Member member = new Member("test1234", "John Doe", "01012345678");
    Member savedMember = memberRepository.save(member);
    Member foundMember = memberRepository.findByPhoneNumber("01012345678");

    assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    assertThat(foundMember.getName()).isEqualTo(savedMember.getName());
    assertThat(foundMember).isEqualTo(savedMember);
  }
}
