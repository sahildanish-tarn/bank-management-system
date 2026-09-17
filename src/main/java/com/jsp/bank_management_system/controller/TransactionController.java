package com.jsp.bank_management_system.controller;

import com.jsp.bank_management_system.dto.transactionDtos.TransactionRequest;
import com.jsp.bank_management_system.dto.transactionDtos.TransactionResponse;
import com.jsp.bank_management_system.dto.transactionDtos.TransferRequest;
import com.jsp.bank_management_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestBody TransactionRequest request) {

        return transactionService.deposit(request);
    }



    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestBody TransactionRequest request) {

        return transactionService.withdraw(request);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request) {

        return transactionService.transfer(request);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> findById(
            @PathVariable Long id) {

        return transactionService.findById(id);
    }



    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionResponse>
    findByTransactionId(
            @PathVariable String transactionId) {

        return transactionService
                .findByTransactionId(transactionId);
    }



    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>>
    findByAccountNumber(
            @PathVariable String accountNumber) {

        return transactionService
                .findByAccountNumber(accountNumber);
    }
}