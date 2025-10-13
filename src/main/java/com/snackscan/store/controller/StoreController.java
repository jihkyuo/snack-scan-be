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

import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.dto.request.AddStoreProductDto;
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
  @PostMapping
  public ResponseEntity<Long> addStore(@Valid @RequestBody AddStoreDto request) {
    Store store = new Store(request.getName(), request.getAddress());
    Long storeId = storeService.addStore(store);
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

  // 매장 상품 등록
  @PostMapping("/{id}/products")
  public ResponseEntity<Long> addStoreProduct(@PathVariable Long id, @RequestBody AddStoreProductDto request) {
    Long storeProductId = storeService.addStoreProduct(
        id,
        request.getProductId(),
        request.getMinStock(),
        request.getCurrentStock(),
        request.getStorePrice());
    return ResponseEntity.status(HttpStatus.CREATED).body(storeProductId);
  }
}
