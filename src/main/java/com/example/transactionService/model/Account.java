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
  private AccountStatus accountStatus;

  @Column(name = "account_type", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal balance;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public Account(
      String customerId,
      AccountStatus accountStatus,
      AccountType accountType,
      BigDecimal balance
  ) {
    this.customerId = customerId;
    this.accountStatus = accountStatus;
    this.accountType = accountType;
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

  public Boolean withdraw(BigDecimal amount) {
    if (this.accountStatus != AccountStatus.ACTIVE) {
      return false;
    }

    if (!canWithdraw(amount)) {
            return false;
    }

    this.balance = this.balance.subtract(amount);
    return true;
  }

  public Boolean deposit(BigDecimal amount) {
    if (this.accountStatus != AccountStatus.ACTIVE) {
      return false;
    }

    this.balance = this.balance.add(amount);
    return true;
  }

  private boolean canWithdraw(BigDecimal amount) {
    return this.balance.subtract(amount).compareTo(this.accountType.getMinimumBalance()) > 0;
  }

}
