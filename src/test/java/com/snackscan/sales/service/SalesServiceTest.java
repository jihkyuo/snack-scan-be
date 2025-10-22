package com.snackscan.sales.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.member.entity.Member;
import com.snackscan.member.service.MemberService;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.sales.dto.request.SalesUploadRequestDto;
import com.snackscan.sales.entity.Sales;
import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.service.StoreService;

@SpringBootTest
@Transactional
public class SalesServiceTest {

  @Autowired
  private SalesService salesService;

  @Autowired
  private StoreService storeService;

  @Autowired
  private ProductService productService;

  @Autowired
  private MemberService memberService;

  @Test
  void 매출_단건_등록() {
    // given
    Long storeId = createStore();
    Long productId = createProduct();

    SalesUploadRequestDto request = new SalesUploadRequestDto();
    request.setStoreId(storeId);
    request.setProductId(productId);
    request.setQuantity(10);
    request.setUnitPrice(1000);

    // when
    salesService.uploadSales(request);

    // then
    Sales sales = salesService.findSalesByIdOrThrow(1L);
    assertThat(sales.getStore().getId()).isEqualTo(storeId);
    assertThat(sales.getProduct().getId()).isEqualTo(productId);
    assertThat(sales.getQuantity()).isEqualTo(request.getQuantity());
    assertThat(sales.getUnitPrice()).isEqualTo(1000);
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

  private Long createProduct() {
    Product product = productService.createProduct("짱구", "농심", 1000);
    return product.getId();
  }
}
