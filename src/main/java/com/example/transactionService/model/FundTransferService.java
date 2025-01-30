package com.example.transactionService.model;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FundTransferService {

    public Boolean transferFunds(
            Account sourceAccount,
            Account destinationAccount,
            BigDecimal amount
    ) {
        Boolean withdrawalSuccess = sourceAccount.withdraw(amount);
        Boolean depositSuccess = destinationAccount.deposit(amount);
        return withdrawalSuccess && depositSuccess;
    }
}
