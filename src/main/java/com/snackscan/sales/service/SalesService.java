package com.snackscan.sales.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

  private final SalesRepository salesRepository;
  private final StoreService storeService;
  private final ProductService productService;

  // 매출 단건 업로드
  public Long salesUpload(SalesUploadRequestDto request) {
    Store store = storeService.findStoreByIdOrThrow(request.getStoreId());
    Product product = productService.findProductByIdOrThrow(request.getProductId());
    Sales sales = Sales.createSales(
        store,
        product,
        request.getQuantity(),
        request.getUnitPrice());

    salesRepository.save(sales);
    return sales.getId(); 
  }

  // 매출 단건 조회
  public Sales findSalesByIdOrThrow(Long salesId) {
    return salesRepository.findById(salesId)
        .orElseThrow(() -> new BusinessException(SalesErrorCode.SALES_NOT_FOUND));
  }

  // 매출 여러 건 업로드
  public void salesBulkUpload(SalesBulkUploadRequestDto request) {
    // 1. 매장 조회
    Store store = storeService.findStoreByIdOrThrow(request.getStoreId());
    
    // 2. 모든 Product ID를 수집
    Set<Long> productIds = request.getSalesList().stream()
        .map(SalesItemDto::getProductId)
        .collect(Collectors.toSet());
    
    // 3. 한 번에 모든 Product 조회
    Map<Long, Product> productMap = productService.findProductsByIds(productIds)
        .stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    // 4. Sales 엔티티 생성
    List<Sales> sales = request.getSalesList().stream()
        .map(salesRequest -> {
          Product product = productMap.get(salesRequest.getProductId());
          
          if (product == null) {
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
  }

}
