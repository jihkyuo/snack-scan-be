package com.snackscan.supplier.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.supplier.dto.request.AddSupplierRequestDto;
import com.snackscan.supplier.dto.request.EditSupplierRequestDto;
import com.snackscan.supplier.entity.Supplier;
import com.snackscan.supplier.exception.SupplierErrorCode;
import com.snackscan.supplier.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierService {

  private final SupplierRepository supplierRepository;

  // 공급자 등록
  public Long addSupplier(AddSupplierRequestDto request) {
    Supplier supplier = Supplier.create(request.getName(), request.getAddress(), request.getPhoneNumber(),
        request.getEmail(), request.getWebsite());
    supplierRepository.save(supplier);
    return supplier.getId();
  }

  // 공급자 단일 조회
  @Transactional(readOnly = true)
  public Supplier findSupplierByIdOrThrow(Long supplierId) {
    return supplierRepository.findById(supplierId)
        .orElseThrow(() -> new BusinessException(SupplierErrorCode.SUPPLIER_NOT_FOUND));
  }

  // 공급자 전체 조회
  @Transactional(readOnly = true)
  public List<Supplier> findAllSuppliers() {
    return supplierRepository.findAll();
  }

  // 공급자 수정
  public void update(Long supplierId, EditSupplierRequestDto request) {
    Supplier supplier = findSupplierByIdOrThrow(supplierId);
    supplier.update(request.getName(), request.getAddress(), request.getPhoneNumber(), request.getEmail(),
        request.getWebsite());
  }

  // 공급자 삭제
  public void delete(Long supplierId) {
    Supplier supplier = findSupplierByIdOrThrow(supplierId);
    supplierRepository.delete(supplier);
  }

}
