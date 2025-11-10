package com.snackscan.store.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackscan.common.exception.BusinessException;
import com.snackscan.member.entity.Member;
import com.snackscan.member.entity.MemberStoreRole;
import com.snackscan.member.entity.Role;
import com.snackscan.member.repository.MemberStoreRoleRepository;
import com.snackscan.member.service.MemberService;
import com.snackscan.product.entity.Product;
import com.snackscan.product.service.ProductService;
import com.snackscan.store.dto.request.AddStoreDto;
import com.snackscan.store.dto.request.AddStoreProductDto;
import com.snackscan.store.dto.request.AddStoreProductNewDto;
import com.snackscan.store.dto.request.UpdateStoreProductRequestDto;
import com.snackscan.store.entity.Store;
import com.snackscan.store.entity.StoreProduct;
import com.snackscan.store.exception.StoreErrorCode;
import com.snackscan.store.repository.StoreProductRepository;
import com.snackscan.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

  private static final Logger log = LoggerFactory.getLogger(StoreService.class);
  private final StoreRepository storeRepository;
  private final StoreProductRepository storeProductRepository;
  private final ProductService productService;
  private final MemberStoreRoleRepository memberStoreRoleRepository;
  private final MemberService memberService;

  // 매장 등록
  public Long addStore(AddStoreDto request) {
    return addStore(request.getName(), request.getAddress(), request.getMemberId());
  }

  public Long addStore(String name, String address, Long memberId) {
    log.debug("매장 생성 시작 - name: {}, address: {}, memberId: {}", name, address, memberId);
    Member member = memberService.findByIdOrThrow(memberId);

    // 스토어 생성 및 저장
    Store store = Store.createStore(name, address);
    storeRepository.save(store);
    log.debug("매장 저장 완료 - storeId: {}", store.getId());

    // 멤버-스토어 관계 생성 및 저장 (스토어 생성자는 자동으로 OWNER 역할)
    MemberStoreRole memberStoreRole = MemberStoreRole.createMemberStoreRelation(member, store, Role.OWNER);

    // 관계 저장
    memberStoreRoleRepository.save(memberStoreRole);
    log.debug("매장-회원 관계 저장 완료 - storeId: {}, memberId: {}, role: OWNER", store.getId(), memberId);

    return store.getId();
  }

  // 매장 전체 조회
  @Transactional(readOnly = true)
  public List<Store> findAllStores() {
    return storeRepository.findAll();
  }

  // 매장 삭제
  public void deleteStore(Long storeId) {
    log.debug("매장 삭제 시작 - storeId: {}", storeId);
    Store store = findStoreByIdOrThrow(storeId);
    storeRepository.delete(store);
    log.debug("매장 삭제 완료 - storeId: {}", storeId);
  }

  // 매장 ID로 조회, 없으면 예외 발생
  @Transactional(readOnly = true)
  public Store findStoreByIdOrThrow(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));
  }

  // 매장 상품 조회
  @Transactional(readOnly = true)
  public List<StoreProduct> findStoreProducts(Long storeId) {
    return storeProductRepository.findByStoreId(storeId);
  }

  // 매장 상품 등록 (기존 Product 사용)
  public Long addStoreProduct(Long storeId, AddStoreProductDto request) {
    log.debug("매장 상품 등록 시작 - storeId: {}, productId: {}", storeId, request.getProductId());
    // Store 조회
    Store store = findStoreByIdOrThrow(storeId);

    // Product 조회
    Product product = productService.findProductByIdOrThrow(request.getProductId());

    // StoreProduct 생성 및 저장
    StoreProduct storeProduct = StoreProduct.createStoreProduct(
        request.getMinStock(),
        request.getCurrentStock(),
        request.getSupplementStock(),
        request.getStorePrice(),
        product,
        store);

    storeProductRepository.save(storeProduct);
    log.debug("매장 상품 등록 완료 - storeId: {}, storeProductId: {}", storeId, storeProduct.getId());
    return storeProduct.getId();
  }

  // 매장 상품 등록 (새 Product 생성)
  public Long addStoreProductNew(Long storeId, AddStoreProductNewDto request) {
    // Store 조회
    Store store = findStoreByIdOrThrow(storeId);

    // Product 생성
    Product product = productService.createProduct(
        request.getProductName(),
        request.getProductBrand(),
        request.getProductPrice());

    // StoreProduct 생성 및 저장
    StoreProduct storeProduct = StoreProduct.createStoreProduct(
        request.getMinStock(),
        request.getCurrentStock(),
        request.getSupplementStock(),
        request.getStorePrice(),
        product,
        store);

    storeProductRepository.save(storeProduct);
    return storeProduct.getId();
  }

  // 매장 상품 삭제
  public void deleteStoreProduct(Long storeProductId) {
    StoreProduct storeProduct = findStoreProductByIdOrThrow(storeProductId);
    storeProductRepository.delete(storeProduct);
  }

  // 매장 상품 단일 조회
  @Transactional(readOnly = true)
  public StoreProduct findStoreProductByIdOrThrow(Long storeProductId) {
    return storeProductRepository.findById(storeProductId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_PRODUCT_NOT_FOUND));
  }

  // 매장 오너 조회
  @Transactional(readOnly = true)
  public Member findStoreOwner(Long storeId) {
    List<MemberStoreRole> ownerRoles = memberStoreRoleRepository.findByStoreIdAndStoreRole(storeId, Role.OWNER);
    if (ownerRoles.isEmpty()) {
      throw new BusinessException(StoreErrorCode.STORE_OWNER_NOT_FOUND);
    }

    if (ownerRoles.size() > 1) {
      throw new BusinessException(StoreErrorCode.STORE_OWNER_NOT_UNIQUE);
    }

    return ownerRoles.get(0).getMember();
  }

  // 매장 직원 추가
  public void addStoreEmployee(Long storeId, List<Long> memberIds) {
    log.debug("매장 직원 추가 시작 - storeId: {}, memberIds: {}", storeId, memberIds);
    Store store = findStoreByIdOrThrow(storeId);

    // 모든 멤버를 한 번에 조회
    List<Member> members = memberService.findByIds(memberIds);
    if (members.size() != memberIds.size()) {
      log.warn("일부 회원을 찾을 수 없음 - storeId: {}, 요청된 회원 수: {}, 조회된 회원 수: {}", 
          storeId, memberIds.size(), members.size());
      throw new BusinessException(StoreErrorCode.MEMBER_NOT_FOUND);
    }

    // 모든 중복 체크
    if (memberStoreRoleRepository.hasExistingMembers(memberIds, storeId)) {
      log.warn("이미 매장에 등록된 회원 존재 - storeId: {}, memberIds: {}", storeId, memberIds);
      throw new BusinessException(StoreErrorCode.MEMBER_ALREADY_IN_STORE);
    }

    List<MemberStoreRole> memberStoreRoles = members.stream()
        .map(member -> MemberStoreRole.createMemberStoreRelation(member, store, Role.EMPLOYEE))
        .toList();

    // 배치로 한 번에 저장
    memberStoreRoleRepository.saveAll(memberStoreRoles);
    log.debug("매장 직원 추가 완료 - storeId: {}, 추가된 직원 수: {}", storeId, memberStoreRoles.size());
  }

  // 매장 직원 조회
  @Transactional(readOnly = true)
  public List<MemberStoreRole> findStoreEmployees(Long storeId) {
    return memberStoreRoleRepository.findByStoreIdAndStoreRole(storeId, Role.EMPLOYEE);
  }

  // 매장 상품 수정
  public void updateStoreProduct(Long storeProductId, UpdateStoreProductRequestDto request) {
    log.debug("매장 상품 수정 시작 - storeProductId: {}", storeProductId);
    StoreProduct storeProduct = findStoreProductByIdOrThrow(storeProductId);
    storeProduct.updateInfo(request.getMinStock(), request.getCurrentStock(), request.getSupplementStock(), request.getStorePrice());
    storeProductRepository.save(storeProduct);
    log.debug("매장 상품 수정 완료 - storeProductId: {}", storeProductId);
  }

  // 매장 상품 조회
  @Transactional(readOnly = true)
  public StoreProduct findStoreProductByStoreIdAndProductId(Long storeId, Long productId) {
    return storeProductRepository.findByStoreIdAndProductId(storeId, productId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_PRODUCT_NOT_FOUND));
  }

  // 매장 상품 일괄 조회 (최적화용)
  @Transactional(readOnly = true)
  public List<StoreProduct> findStoreProductsByStoreIdAndProductIds(Long storeId, List<Long> productIds) {
    return storeProductRepository.findByStoreIdAndProductIdIn(storeId, productIds);
  }
}
