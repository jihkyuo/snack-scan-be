package com.snackscan.store.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.entity.StoreProduct;

@SpringBootTest
@Transactional
public class StoreServiceTest {

  @Autowired
  private StoreService storeService;

  @Autowired  
  private ProductService productService;

  @Test
  void 매장_상품_등록() {
    // given

    // 매장 등록
    AddStoreDto request = new AddStoreDto();
    request.setName("매장1");
    request.setAddress("서울");
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
