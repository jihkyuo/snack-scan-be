package com.snackscan.store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackscan.member.entity.MemberStoreRole;
import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.dto.request.AddStoreEmployeeDto;
import com.snackscan.store.dto.request.AddStoreProductDto;
import com.snackscan.store.dto.request.AddStoreProductNewDto;
import com.snackscan.store.dto.response.StoreEmployeeResponseDto;
import com.snackscan.store.dto.response.StoreProductResponseDto;
import com.snackscan.store.dto.response.StoreResponseDto;
import com.snackscan.store.entity.Store;
import com.snackscan.store.entity.StoreProduct;
import com.snackscan.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  // 매장 등록
  // todo 추후 로그인한 토큰으로 사용자 식별
  @PostMapping
  public ResponseEntity<Long> addStore(@Valid @RequestBody AddStoreDto request) {
    Long storeId = storeService.addStore(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeId);
  }

  // 매장 전체 조회
  @GetMapping
  public ResponseEntity<List<StoreResponseDto>> findAllStores() {
    List<Store> stores = storeService.findAllStores();
    return ResponseEntity.ok(stores.stream()
        .map(StoreResponseDto::new)
        .toList());
  }

  // 매장 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
    storeService.deleteStore(id);
    return ResponseEntity.noContent().build();
  }

  // 매장 상품 조회
  @GetMapping("/{id}/products")
  public ResponseEntity<List<StoreProductResponseDto>> findStoreProducts(@PathVariable Long id) {
    List<StoreProduct> storeProducts = storeService.findStoreProducts(id);
    return ResponseEntity.ok(storeProducts.stream()
        .map(StoreProductResponseDto::new)
        .toList());
  }

  // 매장 상품 등록(기존 상품 사용)
  @PostMapping("/{id}/products")
  public ResponseEntity<Long> addStoreProduct(
      @PathVariable Long id,
      @Valid @RequestBody AddStoreProductDto request) {
    Long storeProductId = storeService.addStoreProduct(id, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeProductId);
  }

  // 매장 상품 등록(새 상품 생성)
  @PostMapping("/{id}/products/new")
  public ResponseEntity<Long> addStoreProductNew(
      @PathVariable Long id,
      @Valid @RequestBody AddStoreProductNewDto request) {
    Long storeProductId = storeService.addStoreProductNew(id, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeProductId);
  }

  // 매장 직원 조회
  @GetMapping("/{id}/employees")
  public ResponseEntity<List<StoreEmployeeResponseDto>> findStoreEmployees(@PathVariable Long id) {
    List<MemberStoreRole> storeEmployees = storeService.findStoreEmployees(id);
    return ResponseEntity.ok(storeEmployees.stream()
        .map(StoreEmployeeResponseDto::new)
        .toList());
  }

  // 매장 직원 추가
  @PostMapping("/{id}/employees")
  public ResponseEntity<Void> addStoreEmployee(@PathVariable Long id, @Valid @RequestBody AddStoreEmployeeDto request) {
    storeService.addStoreEmployee(id, request.getMemberIds());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
