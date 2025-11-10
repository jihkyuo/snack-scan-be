package com.snackscan.product.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.product.entity.Product;
import com.snackscan.product.exception.ProductErrorCode;
import com.snackscan.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);
  private final ProductRepository productRepository;

  // Product ID로 조회, 없으면 예외 발생
  @Transactional(readOnly = true)
  public Product findProductByIdOrThrow(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
  }

  // Product ID 목록으로 일괄 조회
  @Transactional(readOnly = true)
  public List<Product> findProductsByIds(Set<Long> productIds) {
    return productRepository.findAllById(productIds);
  }

  // Product 생성
  public Product createProduct(String name, String brand, int productPrice) {
    log.debug("상품 생성 시작 - name: {}, brand: {}, price: {}", name, brand, productPrice);
    validateDuplicateProduct(name);
    Product product = Product.createProduct(name, brand, productPrice);
    Product savedProduct = productRepository.save(product);
    log.debug("상품 생성 완료 - productId: {}", savedProduct.getId());
    return savedProduct;
  }

  private void validateDuplicateProduct(String name) {
    log.debug("중복 상품 검증 시작 - name: {}", name);
    Product findProduct = productRepository.findByName(name);
    if (findProduct != null) {
      log.warn("중복 상품 생성 시도 - name: {}", name);
      throw new BusinessException(ProductErrorCode.DUPLICATE_PRODUCT);
    }
    log.debug("중복 상품 검증 통과 - name: {}", name);
  }
}
