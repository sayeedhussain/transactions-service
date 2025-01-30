package com.example.transactionService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "customer_id", nullable = false, unique = true, length = 20)
  private String customerId;

  @Setter
  @Column(name = "account_status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private AccountStatus status;

  @Column(name = "minimum_balance", nullable = false, precision = 10, scale = 2)
  private BigDecimal minimumBalance;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal balance;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public Account(
      String customerId,
      AccountStatus status,
      BigDecimal minimumBalance,
      BigDecimal balance) {
    this.customerId = customerId;
    this.status = status;
    this.minimumBalance = minimumBalance;
    this.balance = balance;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
