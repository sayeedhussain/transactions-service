package com.example.transactionService;

import com.example.transactionService.db.AccountRepository;
import com.example.transactionService.model.Account;
import com.example.transactionService.model.FundTransferService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionsService {

  private final AccountRepository accountRepository;
  private final FundTransferService fundTransferService;

  public TransactionsService(
          AccountRepository accountRepository,
          FundTransferService fundTransferService
  ) {
    this.accountRepository = accountRepository;
    this.fundTransferService = fundTransferService;
  }

  /*
  @Transactional ensures that all repository operations within the method are part of the same transaction. If one operation fails, the entire transaction will be rolled back.
   */
  @Transactional
  public Boolean transferFunds(
          Long sourceAccountId,
          Long destinationAccountId,
          BigDecimal amount
  ) {
    Account sourceAccount = accountRepository.getReferenceById(sourceAccountId);
    Account destinationAccount = accountRepository.getReferenceById(destinationAccountId);

    Boolean success = fundTransferService.transferFunds(sourceAccount, destinationAccount, amount);
    if (!success) {
      return false;
    }

    accountRepository.save(sourceAccount);
    accountRepository.save(destinationAccount);

    return true;
  }
}
