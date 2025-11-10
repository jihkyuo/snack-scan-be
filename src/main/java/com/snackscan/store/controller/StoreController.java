package com.snackscan.store.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackscan.member.entity.MemberStoreRole;
import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.dto.request.AddStoreEmployeeDto;
import com.snackscan.store.dto.request.AddStoreProductDto;
import com.snackscan.store.dto.request.AddStoreProductNewDto;
import com.snackscan.store.dto.request.UpdateStoreProductRequestDto;
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

  private static final Logger log = LoggerFactory.getLogger(StoreController.class);
  private final StoreService storeService;

  // 매장 등록
  // todo 추후 로그인한 토큰으로 사용자 식별
  @PostMapping
  public ResponseEntity<Long> addStore(@Valid @RequestBody AddStoreDto request) {
    log.info("매장 등록 요청 - name: {}, address: {}, memberId: {}",
        request.getName(), request.getAddress(), request.getMemberId());
    Long storeId = storeService.addStore(request);
    log.info("매장 등록 완료 - storeId: {}", storeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeId);
  }

  // 매장 전체 조회
  @GetMapping
  public ResponseEntity<List<StoreResponseDto>> findAllStores() {
    log.info("매장 전체 조회 요청");
    List<Store> stores = storeService.findAllStores();
    List<StoreResponseDto> response = stores.stream()
        .map(StoreResponseDto::new)
        .toList();
    log.info("매장 전체 조회 완료 - 조회된 매장 수: {}", response.size());
    return ResponseEntity.ok(response);
  }

  // 매장 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
    log.info("매장 삭제 요청 - storeId: {}", id);
    storeService.deleteStore(id);
    log.info("매장 삭제 완료 - storeId: {}", id);
    return ResponseEntity.noContent().build();
  }

  // 매장 상품 조회
  @GetMapping("/{id}/products")
  public ResponseEntity<List<StoreProductResponseDto>> findStoreProducts(@PathVariable Long id) {
    log.info("매장 상품 조회 요청 - storeId: {}", id);
    List<StoreProduct> storeProducts = storeService.findStoreProducts(id);
    List<StoreProductResponseDto> response = storeProducts.stream()
        .map(StoreProductResponseDto::new)
        .toList();
    log.info("매장 상품 조회 완료 - storeId: {}, 조회된 상품 수: {}", id, response.size());
    return ResponseEntity.ok(response);
  }

  // 매장 상품 등록(기존 상품 사용)
  @PostMapping("/{id}/products")
  public ResponseEntity<Long> addStoreProduct(
      @PathVariable Long id,
      @Valid @RequestBody AddStoreProductDto request) {
    log.info("매장 상품 등록 요청 - storeId: {}, productId: {}", id, request.getProductId());
    Long storeProductId = storeService.addStoreProduct(id, request);
    log.info("매장 상품 등록 완료 - storeId: {}, storeProductId: {}", id, storeProductId);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeProductId);
  }

  // 매장 상품 등록(새 상품 생성)
  @PostMapping("/{id}/products/new")
  public ResponseEntity<Long> addStoreProductNew(
      @PathVariable Long id,
      @Valid @RequestBody AddStoreProductNewDto request) {
    log.info("매장 새 상품 등록 요청 - storeId: {}, productName: {}", id, request.getProductName());
    Long storeProductId = storeService.addStoreProductNew(id, request);
    log.info("매장 새 상품 등록 완료 - storeId: {}, storeProductId: {}", id, storeProductId);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeProductId);
  }

  // 매장 상품 수정
  @PutMapping("/products/{storeProductId}")
  public ResponseEntity<Void> updateStoreProduct(
      @PathVariable Long storeProductId,
      @Valid @RequestBody UpdateStoreProductRequestDto request) {
    log.info("매장 상품 수정 요청 - storeProductId: {}", storeProductId);
    storeService.updateStoreProduct(storeProductId, request);
    log.info("매장 상품 수정 완료 - storeProductId: {}", storeProductId);
    return ResponseEntity.noContent().build();
  }

  // 매장 직원 조회
  @GetMapping("/{id}/employees")
  public ResponseEntity<List<StoreEmployeeResponseDto>> findStoreEmployees(@PathVariable Long id) {
    log.info("매장 직원 조회 요청 - storeId: {}", id);
    List<MemberStoreRole> storeEmployees = storeService.findStoreEmployees(id);
    List<StoreEmployeeResponseDto> response = storeEmployees.stream()
        .map(StoreEmployeeResponseDto::new)
        .toList();
    log.info("매장 직원 조회 완료 - storeId: {}, 조회된 직원 수: {}", id, response.size());
    return ResponseEntity.ok(response);
  }

  // 매장 직원 추가
  @PostMapping("/{id}/employees")
  public ResponseEntity<Void> addStoreEmployee(@PathVariable Long id, @Valid @RequestBody AddStoreEmployeeDto request) {
    log.info("매장 직원 추가 요청 - storeId: {}, memberIds: {}", id, request.getMemberIds());
    storeService.addStoreEmployee(id, request.getMemberIds());
    log.info("매장 직원 추가 완료 - storeId: {}, 추가된 직원 수: {}", id, request.getMemberIds().size());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
