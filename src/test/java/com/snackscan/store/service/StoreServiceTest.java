package com.snackscan.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.member.entity.Member;
import com.snackscan.member.entity.MemberStoreRole;
import com.snackscan.member.service.MemberService;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.entity.Store;
import com.snackscan.store.entity.StoreProduct;

@SpringBootTest
@Transactional
public class StoreServiceTest {

  @Autowired
  private StoreService storeService;

  @Autowired
  private ProductService productService;

  @Autowired
  private MemberService memberService;

  @Test
  void 매장_등록() {
    // given
    // when
    Long storeId = createStore();

    // then
    Store store = storeService.findStoreByIdOrThrow(storeId);
    assertThat(store.getId()).isEqualTo(storeId);
    assertThat(store.getName()).isEqualTo("매장1");
    assertThat(store.getAddress()).isEqualTo("서울");
    // 스토어 사장 확인
    Member foundOwnerMember = storeService.findStoreOwner(storeId);
    assertThat(foundOwnerMember.getName()).isEqualTo("홍길동");
  }

  @Test
  void 매장_상품_등록() {
    // given
    Long storeId = createStore();

    // 상품 등록
    Product product = productService.createProduct("짱구", "농심", 1000);

    // when
    Long storeProductId = storeService.addStoreProduct(
        storeId,
        product.getId(),
        10,
        10,
        1000);

    // then
    StoreProduct storeProduct = storeService.findStoreProductByIdOrThrow(storeProductId);
    assertThat(storeProduct.getId()).isEqualTo(storeProductId);
    assertThat(storeProduct.getMinStock()).isEqualTo(10);
    assertThat(storeProduct.getCurrentStock()).isEqualTo(10);
    assertThat(storeProduct.getStorePrice()).isEqualTo(1000);
  }

  @Test
  void 매장_직원_추가() {
    // given
    Long storeId = createStore();
    Member employeeMember1 = createMember("jio2", "직원1", "01012345678");
    Member employeeMember2 = createMember("jio3", "직원2", "01012345678");

    // when
    storeService.addStoreEmployee(storeId, List.of(
        employeeMember1.getId(),
        employeeMember2.getId()));

    // then
    List<MemberStoreRole> employeeMembers = storeService.findStoreEmployees(storeId);
    assertThat(employeeMembers.size()).isEqualTo(2);
    assertThat(employeeMembers.get(0).getMember().getName()).isEqualTo("직원1");
    assertThat(employeeMembers.get(1).getMember().getName()).isEqualTo("직원2");
  }

  @Test
  void 매장_직원_추가_중복_예외() {
    // given
    Long storeId = createStore();
    Member employeeMember = createMember("jio2", "직원1", "01012345678");
    storeService.addStoreEmployee(storeId, List.of(employeeMember.getId()));

    assertThatThrownBy(() ->
    // when
    storeService.addStoreEmployee(storeId, List.of(employeeMember.getId())))
        // then
        .isInstanceOf(BusinessException.class)
        .hasMessage("이미 해당 매장에 소속된 직원입니다.");
  }

  private Member createMember(String loginId, String name, String phoneNumber) {
    Member member = new Member(loginId, name, phoneNumber);
    memberService.join(member);
    return member;
  }

  private Long createStore() {
    Member member = createMember("jio", "홍길동", "01012345678");

    AddStoreDto request = new AddStoreDto();
    request.setName("매장1");
    request.setAddress("서울");
    request.setMemberId(member.getId());
    Long storeId = storeService.addStore(request);
    return storeId;
  }

}
