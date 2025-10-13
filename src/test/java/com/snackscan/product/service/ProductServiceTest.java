package com.snackscan.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.product.entity.Product;

@SpringBootTest
@Transactional
public class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Test
  void 상품_생성() {
    // given
    // when
    Product createdProduct = productService.createProduct("짱구", "농심", 1000);

    // then
    Product foundProduct = productService.findProductByIdOrThrow(createdProduct.getId());
    assertThat(foundProduct.getId()).isEqualTo(createdProduct.getId());
    assertThat(foundProduct.getName()).isEqualTo(createdProduct.getName());
    assertThat(foundProduct.getBrand()).isEqualTo(createdProduct.getBrand());
    assertThat(foundProduct.getProductPrice()).isEqualTo(createdProduct.getProductPrice());
  }

  @Test
  void 중복_상품_예외() {
    // given
    productService.createProduct("짱구", "농심", 1000);
    // when
    assertThatThrownBy(() -> productService.createProduct("짱구", "농심", 1000))
        .isInstanceOf(BusinessException.class)
        .hasMessage("이미 존재하는 상품입니다.");
  }

}
