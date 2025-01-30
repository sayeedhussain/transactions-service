package com.example.transactionService.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum AccountType {
  REGULAR_SAVINGS_ACCOUNT(BigDecimal.valueOf(10000)),
  HIGH_INTEREST_SAVINGS_ACCOUNT(BigDecimal.valueOf(20000)),
  STUDENTS_SAVINGS_ACCOUNT(BigDecimal.valueOf(5000)),
  SENIOR_CITIZENS_SAVINGS_ACCOUNT(BigDecimal.valueOf(5000));

  private final BigDecimal minimumBalance;

  AccountType(BigDecimal minimumBalance) {
    this.minimumBalance = minimumBalance;
  }

}
