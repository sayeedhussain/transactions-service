package com.example.transactionService.model;

import java.math.BigDecimal;

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
