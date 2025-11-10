package com.snackscan.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackscan.product.dto.request.AddProductRequestDto;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private static final Logger log = LoggerFactory.getLogger(ProductController.class);
  private final ProductService productService;

  // 상품 등록
  @PostMapping
  public ResponseEntity<Long> addProduct(@Valid @RequestBody AddProductRequestDto request) {
    log.info("상품 등록 요청 - name: {}, brand: {}, price: {}", 
        request.getName(), request.getBrand(), request.getProductPrice());
    Product product = productService.createProduct(request.getName(), request.getBrand(), request.getProductPrice());
    log.info("상품 등록 완료 - productId: {}", product.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(product.getId());
  }

}
