package com.snackscan.sales.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackscan.sales.dto.request.SalesRequestDto;
import com.snackscan.sales.dto.response.SalesResponseDto;
import com.snackscan.sales.entity.Sales;
import com.snackscan.sales.service.SalesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

  private static final Logger log = LoggerFactory.getLogger(SalesController.class);
  private final SalesService salesService;

  // 매출 일괄 업로드
  @PostMapping("/stores/{storeId}/bulk")
  public ResponseEntity<Void> salesBulkUpload(@PathVariable Long storeId,
      @Valid @RequestBody List<SalesRequestDto> salesList) {
    log.info("매출 일괄 업로드 요청 - storeId: {}, 매출 건수: {}", storeId, salesList.size());
    salesService.salesBulkUpload(storeId, salesList);
    log.info("매출 일괄 업로드 완료 - storeId: {}, 매출 건수: {}", storeId, salesList.size());
    return ResponseEntity.ok().build();
  }

  // 매출 단건 업로드
  @PostMapping("/stores/{storeId}")
  public ResponseEntity<Void> salesUpload(@PathVariable Long storeId,
      @Valid @RequestBody SalesRequestDto request) {
    log.info("매출 단건 업로드 요청 - storeId: {}, 매출 건수: {}", storeId, request);
    salesService.salesUpload(storeId, request);
    log.info("매출 단건 업로드 완료 - storeId: {}, 매출 건수: {}", storeId, request);
    return ResponseEntity.ok().build();
  }

  // 매출 조회
  @GetMapping("/stores/{storeId}")
  public ResponseEntity<List<SalesResponseDto>> findSalesByStoreId(@PathVariable Long storeId) {
    log.info("매출 조회 요청 - storeId: {}", storeId);
    List<Sales> sales = salesService.findSalesByStoreId(storeId);
    List<SalesResponseDto> response = sales.stream()
        .map(SalesResponseDto::new)
        .toList();
    log.info("매출 조회 완료 - storeId: {}, 조회된 매출 건수: {}", storeId, response.size());
    return ResponseEntity.ok(response);
  }
}
