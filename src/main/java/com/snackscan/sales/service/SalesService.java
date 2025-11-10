package com.snackscan.sales.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.sales.dto.request.SalesBulkUploadRequestDto;
import com.snackscan.sales.dto.request.SalesItemDto;
import com.snackscan.sales.dto.request.SalesUploadRequestDto;
import com.snackscan.sales.entity.Sales;
import com.snackscan.sales.exception.SalesErrorCode;
import com.snackscan.sales.repository.SalesRepository;
import com.snackscan.store.entity.Store;
import com.snackscan.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SalesService {

  private static final Logger log = LoggerFactory.getLogger(SalesService.class);
  private final SalesRepository salesRepository;
  private final StoreService storeService;
  private final ProductService productService;

  // 매출 단건 업로드
  public Long salesUpload(SalesUploadRequestDto request) {
    log.debug("매출 단건 업로드 시작 - storeId: {}, productId: {}, quantity: {}, unitPrice: {}", 
        request.getStoreId(), request.getProductName(), request.getQuantity(), request.getUnitPrice());
    Store store = storeService.findStoreByIdOrThrow(request.getStoreId());
    Product product = productService.findProductByNameOrThrow(request.getProductName());
    Sales sales = Sales.createSales(
        store,
        product,
        request.getQuantity(),
        request.getUnitPrice());

    salesRepository.save(sales);
    log.debug("매출 단건 업로드 완료 - salesId: {}", sales.getId());
    return sales.getId(); 
  }

  // 매출 단건 조회
  public Sales findSalesByIdOrThrow(Long salesId) {
    return salesRepository.findById(salesId)
        .orElseThrow(() -> new BusinessException(SalesErrorCode.SALES_NOT_FOUND));
  }

  // 매출 여러 건 업로드
  public void salesBulkUpload(SalesBulkUploadRequestDto request) {
    log.info("매출 일괄 업로드 시작 - storeId: {}, 매출 건수: {}", 
        request.getStoreId(), 
        request.getSalesList() != null ? request.getSalesList().size() : 0);
    
    // 1. 매장 조회
    Store store = storeService.findStoreByIdOrThrow(request.getStoreId());
    
    // 2. 매출 목록 검증
    if (request.getSalesList() == null || request.getSalesList().isEmpty()) {
      log.warn("매출 목록이 비어있음 - storeId: {}", request.getStoreId());
      throw new BusinessException(SalesErrorCode.SALES_LIST_EMPTY);
    }
    
    // 3. 모든 Product Name을 수집
    Set<String> productNames = request.getSalesList().stream()
        .map(SalesItemDto::getProductName)
        .collect(Collectors.toSet());
    
    log.debug("조회할 상품 Name 수: {}", productNames.size());
    
    // 3. 한 번에 모든 Product 조회
    Map<String, Product> productMap = productService.findProductsByNames(productNames)
        .stream()
        .collect(Collectors.toMap(Product::getName, Function.identity()));
    
    // 4. Sales 엔티티 생성
    List<Sales> sales = request.getSalesList().stream()
        .map(salesRequest -> {
          Product product = productMap.get(salesRequest.getProductName());
          
          if (product == null) {
            log.warn("상품을 찾을 수 없음 - productName: {}", salesRequest.getProductName());
            throw new BusinessException(SalesErrorCode.PRODUCT_NOT_FOUND);
          }
          
          return Sales.createSales(
              store,
              product,
              salesRequest.getQuantity(),
              salesRequest.getUnitPrice());
        })
        .toList();
    
    // 5. 일괄 저장
    salesRepository.saveAll(sales);
    log.info("매출 일괄 업로드 완료 - storeId: {}, 저장된 매출 건수: {}", request.getStoreId(), sales.size());
  }

}
