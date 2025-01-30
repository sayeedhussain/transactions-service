package com.example.transactionService.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionService.domain.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
