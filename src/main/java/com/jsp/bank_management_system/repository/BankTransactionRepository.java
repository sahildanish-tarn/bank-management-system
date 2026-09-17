package com.jsp.bank_management_system.repository;


import com.jsp.bank_management_system.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankTransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    Optional<BankTransaction> findByTransactionId(String transactionId);

    List<BankTransaction> findByAccountAccountNumber(String accountNumber);

    boolean existsByTransactionId(String transactionId);
}