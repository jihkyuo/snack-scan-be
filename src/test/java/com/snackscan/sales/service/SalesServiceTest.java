package com.snackscan.sales.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.member.entity.Member;
import com.snackscan.member.service.MemberService;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.sales.dto.request.SalesBulkUploadRequestDto;
import com.snackscan.sales.dto.request.SalesItemDto;
import com.snackscan.sales.dto.request.SalesUploadRequestDto;
import com.snackscan.sales.entity.Sales;
import com.snackscan.sales.exception.SalesErrorCode;
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

  // 테스트 픽스처
  private TestDataBuilder testDataBuilder;

  @BeforeEach
  void setUp() {
    testDataBuilder = new TestDataBuilder();
  }

  @Nested
  @DisplayName("매출 단건 등록 테스트")
  class SingleSalesUploadTest {

    @Test
    @DisplayName("정상적인 매출 단건 등록")
    void 정상적인_매출_단건_등록() {
      // given
      TestData testData = testDataBuilder.build();
      SalesUploadRequestDto request = new SalesUploadRequestDto();
      request.setStoreId(testData.storeId);
      request.setProductId(testData.productId);
      request.setQuantity(10);
      request.setUnitPrice(1000);

      // when
      Long salesId = salesService.salesUpload(request);

      // then
      Sales sales = salesService.findSalesByIdOrThrow(salesId);
      assertThat(sales.getStore().getId()).isEqualTo(testData.storeId);
      assertThat(sales.getProduct().getId()).isEqualTo(testData.productId);
      assertThat(sales.getQuantity()).isEqualTo(10);
      assertThat(sales.getUnitPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("존재하지 않는 매장으로 매출 등록 시 예외 발생")
    void 존재하지_않는_매장으로_매출_등록_시_예외_발생() {
      // given
      TestData testData = testDataBuilder.build();
      SalesUploadRequestDto request = new SalesUploadRequestDto();
      request.setStoreId(999L); // 존재하지 않는 매장 ID
      request.setProductId(testData.productId);
      request.setQuantity(10);
      request.setUnitPrice(1000);

      // when & then
      assertThatThrownBy(() -> salesService.salesUpload(request))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 매출 등록 시 예외 발생")
    void 존재하지_않는_상품으로_매출_등록_시_예외_발생() {
      // given
      TestData testData = testDataBuilder.build();
      SalesUploadRequestDto request = new SalesUploadRequestDto();
      request.setStoreId(testData.storeId);
      request.setProductId(999L); // 존재하지 않는 상품 ID
      request.setQuantity(10);
      request.setUnitPrice(1000);

      // when & then
      assertThatThrownBy(() -> salesService.salesUpload(request))
          .isInstanceOf(BusinessException.class);
    }
  }

  @Nested
  @DisplayName("매출 여러 건 등록 테스트")
  class BulkSalesUploadTest {

    @Test
    @DisplayName("정상적인 매출 여러 건 등록")
    void 정상적인_매출_여러_건_등록() {
      // given
      TestData testData = testDataBuilder.build();
      
      SalesItemDto salesItem1 = SalesItemDto.builder()
          .productId(testData.productId)
          .quantity(10)
          .unitPrice(1000)
          .build();
      
      SalesItemDto salesItem2 = SalesItemDto.builder()
          .productId(testData.productId)
          .quantity(20)
          .unitPrice(2000)
          .build();

      SalesBulkUploadRequestDto request = SalesBulkUploadRequestDto.builder()
          .storeId(testData.storeId)
          .salesList(List.of(salesItem1, salesItem2))
          .build();

      // when
      salesService.salesBulkUpload(request);

      // then
      List<Sales> createdSales = salesRepository.findAll();
      assertThat(createdSales).hasSize(2);
      
      // 각 매출 항목 검증
      assertThat(createdSales).allSatisfy(sales -> {
        assertThat(sales.getStore().getId()).isEqualTo(testData.storeId);
        assertThat(sales.getProduct().getId()).isEqualTo(testData.productId);
      });
      
      // 수량별 검증
      assertThat(createdSales)
          .extracting(Sales::getQuantity)
          .containsExactlyInAnyOrder(10, 20);
      
      assertThat(createdSales)
          .extracting(Sales::getUnitPrice)
          .containsExactlyInAnyOrder(1000, 2000);
    }

    @Test
    @DisplayName("빈 매출 목록으로 등록 시 예외 발생")
    void 빈_매출_목록으로_등록_시_예외_발생() {
      // given
      TestData testData = testDataBuilder.build();
      SalesBulkUploadRequestDto request = SalesBulkUploadRequestDto.builder()
          .storeId(testData.storeId)
          .salesList(List.of()) // 빈 목록
          .build();

      // when & then
      assertThatThrownBy(() -> salesService.salesBulkUpload(request))
          .isInstanceOf(BusinessException.class);
    }
  }

  @Nested
  @DisplayName("매출 조회 테스트")
  class SalesFindTest {

    @Test
    @DisplayName("존재하는 매출 조회")
    void 존재하는_매출_조회() {
      // given
      TestData testData = testDataBuilder.build();
      SalesUploadRequestDto request = new SalesUploadRequestDto();
      request.setStoreId(testData.storeId);
      request.setProductId(testData.productId);
      request.setQuantity(10);
      request.setUnitPrice(1000);
      
      Long salesId = salesService.salesUpload(request);

      // when
      Sales sales = salesService.findSalesByIdOrThrow(salesId);

      // then
      assertThat(sales).isNotNull();
      assertThat(sales.getId()).isEqualTo(salesId);
    }

    @Test
    @DisplayName("존재하지 않는 매출 조회 시 예외 발생")
    void 존재하지_않는_매출_조회_시_예외_발생() {
      // when & then
      assertThatThrownBy(() -> salesService.findSalesByIdOrThrow(999L))
          .isInstanceOf(BusinessException.class)
          .hasFieldOrPropertyWithValue("errorCode", SalesErrorCode.SALES_NOT_FOUND);
    }
  }

  // 테스트 데이터 빌더 클래스
  private class TestDataBuilder {
    private Long storeId;
    private Long productId;

    TestDataBuilder() {
      // 기본 테스트 데이터 생성
      this.storeId = createStore();
      this.productId = createProduct();
    }

    TestData build() {
      return new TestData(storeId, productId);
    }
  }

  // 테스트 데이터 클래스
  private record TestData(Long storeId, Long productId) {}

  // 테스트 헬퍼 메서드들
  private Member createMember(String loginId, String name, String phoneNumber) {
    Member member = new Member(loginId, name, phoneNumber);
    memberService.join(member);
    return member;
  }

  private Long createStore() {
    Member member = createMember("testUser", "테스트사용자", "01012345678");

    AddStoreDto request = new AddStoreDto();
    request.setName("테스트매장");
    request.setAddress("테스트주소");
    request.setMemberId(member.getId());
    return storeService.addStore(request);
  }

  private Long createProduct() {
    Product product = productService.createProduct("테스트상품", "테스트브랜드", 1000);
    return product.getId();
  }
}
