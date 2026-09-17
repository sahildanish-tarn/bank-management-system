package com.jsp.bank_management_system.service;

import com.jsp.bank_management_system.dto.transactionDtos.TransactionRequest;
import com.jsp.bank_management_system.dto.transactionDtos.TransactionResponse;
import com.jsp.bank_management_system.dto.transactionDtos.TransferRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionService {
    //create
    ResponseEntity<TransactionResponse> deposit(TransactionRequest request);

    ResponseEntity<TransactionResponse> withdraw(
            TransactionRequest request);

    ResponseEntity<TransactionResponse> transfer(
            TransferRequest request);

    ResponseEntity<TransactionResponse> findById(
            Long id);

    ResponseEntity<TransactionResponse> findByTransactionId(
            String transactionId);

    ResponseEntity<List<TransactionResponse>> findByAccountNumber(
            String accountNumber);

}
