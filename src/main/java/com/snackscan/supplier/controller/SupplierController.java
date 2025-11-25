package com.snackscan.supplier.controller;

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

import com.snackscan.supplier.dto.request.AddSupplierRequestDto;
import com.snackscan.supplier.dto.request.EditSupplierRequestDto;
import com.snackscan.supplier.dto.response.SupplierResponseDto;
import com.snackscan.supplier.entity.Supplier;
import com.snackscan.supplier.service.SupplierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

  private static final Logger log = LoggerFactory.getLogger(SupplierController.class);
  private final SupplierService supplierService;

  // 공급자 등록
  @PostMapping
  public ResponseEntity<Long> addSupplier(@Valid @RequestBody AddSupplierRequestDto request) {
    log.info("공급자 등록 요청 - {}", request.toString());
    Long supplierId = supplierService.addSupplier(request);
    log.info("공급자 등록 완료 - supplierId: {}", supplierId);
    return ResponseEntity.status(HttpStatus.CREATED).body(supplierId);
  }

  // 공급자 전체 조회
  @GetMapping
  public ResponseEntity<List<SupplierResponseDto>> findAllSuppliers() {
    log.info("공급자 전체 조회 요청");
    List<Supplier> suppliers = supplierService.findAllSuppliers();
    List<SupplierResponseDto> response = suppliers.stream()
        .map(SupplierResponseDto::from)
        .toList();
    log.info("공급자 전체 조회 완료 - 조회된 공급자 수: {}", response.size());
    return ResponseEntity.ok(response);
  }

  // 공급자 단일 조회
  @GetMapping("/{id}")
  public ResponseEntity<SupplierResponseDto> findSupplierById(@PathVariable Long id) {
    log.info("공급자 조회 요청 - supplierId: {}", id);
    Supplier supplier = supplierService.findSupplierByIdOrThrow(id);
    log.info("공급자 조회 완료 - supplierId: {}", id);
    return ResponseEntity.ok(SupplierResponseDto.from(supplier));
  }

  // 공급자 수정
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateSupplier(@PathVariable Long id,
      @Valid @RequestBody EditSupplierRequestDto request) {
    log.info("공급자 수정 요청 - supplierId: {}", id);
    supplierService.update(id, request);
    log.info("공급자 수정 완료 - supplierId: {}", id);
    return ResponseEntity.ok().build();
  }

  // 공급자 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
    log.info("공급자 삭제 요청 - supplierId: {}", id);
    supplierService.delete(id);
    log.info("공급자 삭제 완료 - supplierId: {}", id);
    return ResponseEntity.noContent().build();
  }
}
