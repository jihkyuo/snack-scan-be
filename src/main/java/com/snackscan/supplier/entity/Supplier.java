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

  public static Supplier create(String name, String address, String phoneNumber, String email, String website) {
    Supplier supplier = new Supplier();
    supplier.name = name;
    supplier.address = address;
    supplier.phoneNumber = phoneNumber;
    supplier.email = email;
    supplier.website = website;
    return supplier;
  }

  public void update(String name, String address, String phoneNumber, String email, String website) {
    if (name != null && !name.trim().isEmpty())
      this.name = name;
    if (address != null && !address.trim().isEmpty())
      this.address = address;
    if (phoneNumber != null && !phoneNumber.trim().isEmpty())
      this.phoneNumber = phoneNumber;
    if (email != null && !email.trim().isEmpty())
      this.email = email;
    if (website != null && !website.trim().isEmpty())
      this.website = website;
  }

}
