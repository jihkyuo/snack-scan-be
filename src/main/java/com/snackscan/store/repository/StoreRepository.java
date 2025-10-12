package com.snackscan.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackscan.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
