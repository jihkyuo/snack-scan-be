package com.snackscan.store.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.member.entity.Member;
import com.snackscan.member.entity.Role;
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
    Member member = new Member("jio", "홍길동", "01012345678", Role.OWNER);
    memberService.join(member);

    AddStoreDto request = new AddStoreDto();
    request.setName("매장1");
    request.setAddress("서울");
    request.setMemberId(member.getId());
    Long storeId = storeService.addStore(request);

    // then
    Store store = storeService.findStoreByIdOrThrow(storeId);
    assertThat(store.getId()).isEqualTo(storeId);
    assertThat(store.getName()).isEqualTo("매장1");
    assertThat(store.getAddress()).isEqualTo("서울");
  }

  @Test
  void 매장_상품_등록() {
    // given

    // 멤버 등록
    Member member = new Member("jio", "홍길동", "01012345678", Role.OWNER);
    memberService.join(member);

    // 매장 등록
    AddStoreDto request = new AddStoreDto();
    request.setName("매장1");
    request.setAddress("서울");
    request.setMemberId(member.getId());
    Long storeId = storeService.addStore(request);

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
}
