package com.example.transactionService.model;

import com.example.transactionService.db.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class FundTransferService {

  private final AccountRepository accountRepository;

  public FundTransferService(
      AccountRepository accountRepository
  ) {
    this.accountRepository = accountRepository;
  }

}
