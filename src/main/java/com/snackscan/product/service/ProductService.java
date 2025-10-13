package com.snackscan.product.service;

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

  private final ProductRepository productRepository;

  // Product ID로 조회, 없으면 예외 발생
  public Product findProductByIdOrThrow(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
  }

  // Product 생성
  public Product createProduct(String name, String brand, int productPrice) {
    validateDuplicateProduct(name);
    Product product = Product.createProduct(name, brand, productPrice);
    return productRepository.save(product);
  }

  private void validateDuplicateProduct(String name) {
    Product findProduct = productRepository.findByName(name);
    if (findProduct != null) {
      throw new BusinessException(ProductErrorCode.DUPLICATE_PRODUCT);
    }
  }
}
