package com.snackscan.supplier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Supplier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "supplier_id")
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 100)
  private String address;

  @Column(nullable = false, length = 100)
  private String phoneNumber;

  @Column(nullable = false, length = 100)
  private String email;

  @Column(length = 100)
  private String website;

  public static Supplier createSupplier(String name, String address, String phoneNumber, String email, String website) {
    Supplier supplier = new Supplier();
    supplier.name = name;
    supplier.address = address;
    supplier.phoneNumber = phoneNumber;
    supplier.email = email;
    supplier.website = website;
    return supplier;
  }

}
