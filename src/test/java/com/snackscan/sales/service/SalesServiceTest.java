package com.snackscan.sales.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.member.entity.Member;
import com.snackscan.member.service.MemberService;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.sales.dto.request.SalesBulkUploadRequestDto;
import com.snackscan.sales.dto.request.SalesItemDto;
import com.snackscan.sales.dto.request.SalesUploadRequestDto;
import com.snackscan.sales.entity.Sales;
import com.snackscan.sales.repository.SalesRepository;
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

  @Autowired
  private SalesRepository salesRepository;

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
    Long salesId = salesService.salesUpload(request);

    // then
    Sales sales = salesService.findSalesByIdOrThrow(salesId);
    assertThat(sales.getStore().getId()).isEqualTo(storeId);
    assertThat(sales.getProduct().getId()).isEqualTo(productId);
    assertThat(sales.getQuantity()).isEqualTo(request.getQuantity());
    assertThat(sales.getUnitPrice()).isEqualTo(request.getUnitPrice());
  }

  @Test
  void 매출_여러_건_등록() {
    // given
    Long storeId = createStore();
    Long productId = createProduct();

    SalesItemDto salesItemDto1 = new SalesItemDto();
    salesItemDto1.setProductId(productId);
    salesItemDto1.setQuantity(10);
    salesItemDto1.setUnitPrice(1000);
    
    SalesItemDto salesItemDto2 = new SalesItemDto();
    salesItemDto2.setProductId(productId);
    salesItemDto2.setQuantity(20);
    salesItemDto2.setUnitPrice(2000);

    SalesBulkUploadRequestDto request = new SalesBulkUploadRequestDto();
    request.setStoreId(storeId);
    request.setSalesList(List.of(salesItemDto1, salesItemDto2));

    // when
    salesService.salesBulkUpload(request);

    // then
    // 모든 매출 조회하여 검증
    List<Sales> allSales = salesRepository.findAll();
    assertThat(allSales).hasSize(2);
    
    // 각 매출 항목이 올바르게 저장되었는지 검증
    List<Sales> createdSales = allSales.stream()
        .filter(sales -> sales.getStore().getId().equals(storeId))
        .toList();
    
    assertThat(createdSales).hasSize(2);
    
    // 첫 번째 매출 검증
    Sales firstSales = createdSales.stream()
        .filter(sales -> sales.getQuantity() == 10)
        .findFirst()
        .orElseThrow(() -> new AssertionError("첫 번째 매출을 찾을 수 없습니다"));
    
    assertThat(firstSales.getStore().getId()).isEqualTo(storeId);
    assertThat(firstSales.getProduct().getId()).isEqualTo(productId);
    assertThat(firstSales.getQuantity()).isEqualTo(10);
    assertThat(firstSales.getUnitPrice()).isEqualTo(1000);
    
    // 두 번째 매출 검증
    Sales secondSales = createdSales.stream()
        .filter(sales -> sales.getQuantity() == 20)
        .findFirst()
        .orElseThrow(() -> new AssertionError("두 번째 매출을 찾을 수 없습니다"));
    
    assertThat(secondSales.getStore().getId()).isEqualTo(storeId);
    assertThat(secondSales.getProduct().getId()).isEqualTo(productId);
    assertThat(secondSales.getQuantity()).isEqualTo(20);
    assertThat(secondSales.getUnitPrice()).isEqualTo(2000);
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
