package com.example.transactionService.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionService.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
