package com.snackscan.sales.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
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
  public void uploadSales(SalesUploadRequestDto request) {
    Store store = storeService.findStoreByIdOrThrow(request.getStoreId());
    Product product = productService.findProductByIdOrThrow(request.getProductId());
    Sales sales = Sales.createSales(
        store,
        product,
        request.getQuantity(),
        request.getUnitPrice());

    salesRepository.save(sales);
  }

  // 매출 단건 조회
  public Sales findSalesByIdOrThrow(Long salesId) {
    return salesRepository.findById(salesId)
        .orElseThrow(() -> new BusinessException(SalesErrorCode.SALES_NOT_FOUND));
  }

}
