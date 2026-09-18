package com.jsp.bank_management_system.service.imp;

import com.jsp.bank_management_system.dto.transactionDtos.TransactionRequest;
import com.jsp.bank_management_system.dto.transactionDtos.TransactionResponse;
import com.jsp.bank_management_system.dto.transactionDtos.TransferRequest;
import com.jsp.bank_management_system.entity.Account;
import com.jsp.bank_management_system.entity.BankTransaction;
import com.jsp.bank_management_system.enums.AccountStatus;
import com.jsp.bank_management_system.enums.TransactionType;
import com.jsp.bank_management_system.exception.*;
import com.jsp.bank_management_system.repository.AccountRepository;
import com.jsp.bank_management_system.repository.BankTransactionRepository;
import com.jsp.bank_management_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BankTransactionRepository bankTransactionRepository;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper ;

    @Override
    public ResponseEntity<TransactionResponse> deposit(TransactionRequest request) {
       if( request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
                throw  new InvalidAmountException("Amount must be greater than 0");
        }
       Account account=accountRepository.findByAccountNumber(request.getAccountNumber())
                                        .orElseThrow(
                                                   () -> new AccountNotFoundException("Account number not found"+ request.getAccountNumber())
                                         );
       validateAccount(account);
       account.setBalance(  account.getBalance().add(request.getAmount()));
       accountRepository.save(account);

       BankTransaction bankTransaction=new BankTransaction();
       bankTransaction.setTransactionId(generateTransactionId());
       bankTransaction.setTransactionType(TransactionType.DEPOSIT);
        bankTransaction.setAmount(request.getAmount());
        bankTransaction.setBalanceAfterTransaction(account.getBalance());
        bankTransaction.setDescription(request.getAmount()+" has been deposited");
        bankTransaction.setCreatedAt( LocalDateTime.now());
       bankTransaction.setAccount(account);

       BankTransaction dbBankTransaction =bankTransactionRepository.save(bankTransaction);

       account.getBankTransactions().add(dbBankTransaction);
       accountRepository.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                objectMapper.convertValue(dbBankTransaction, TransactionResponse.class)
        );
    }

    @Override
    public ResponseEntity<TransactionResponse> withdraw(TransactionRequest request) {
        if( request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw  new RuntimeException("Amount must be greater than 0");
        }
        Account account=accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(
                        () -> new InvalidAmountException("Account number not found: "+request.getAccountNumber())
                );
        validateAccount(account);
        if(account.getBalance().compareTo(request.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        account.setBalance(  account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        BankTransaction bankTransaction=new BankTransaction();
        bankTransaction.setTransactionId(generateTransactionId());
        bankTransaction.setTransactionType(TransactionType.WITHDRAW);
        bankTransaction.setAmount(request.getAmount());
        bankTransaction.setBalanceAfterTransaction(account.getBalance());
        bankTransaction.setDescription(request.getAmount()+" has been withdrawn");
        bankTransaction.setCreatedAt( LocalDateTime.now());
        bankTransaction.setAccount(account);

        BankTransaction dbBankTransaction =bankTransactionRepository.save(bankTransaction);

        account.getBankTransactions().add(dbBankTransaction);
        accountRepository.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                objectMapper.convertValue(dbBankTransaction, TransactionResponse.class)
        );

    }

    @Transactional
    @Override
    public ResponseEntity<TransactionResponse> transfer(TransferRequest request) {

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new SameAccountTransferException("Source and destination accounts cannot be same");
        }

        Account fromAccount = accountRepository
                .findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(
                        () -> new AccountNotFoundException("Account not found with account number: " + request.getFromAccountNumber()
                        )
                );
        Account toAccount = accountRepository
                .findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(
                        () -> new AccountNotFoundException("Account not found with account number: " + request.getToAccountNumber()
                        )
                );

        validateAccount(fromAccount);
        validateAccount(toAccount);

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }

        // Debit from fromAccountNumber
        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(request.getAmount());
        fromAccount.setBalance(fromNewBalance);


        // Credit to toAccountnumber
        BigDecimal toNewBalance = toAccount.getBalance().add(request.getAmount());
        toAccount.setBalance(toNewBalance);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Transaction record
        BankTransaction transaction = new BankTransaction();

        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfterTransaction(fromNewBalance);
        transaction.setDescription("Amount transferred");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccount(fromAccount);
        transaction.setToAccount(toAccount);

        BankTransaction savedTransaction = bankTransactionRepository.save(transaction);


//       //saving transaction in  from account
//        fromAccount.getBankTransactions().add(savedTransaction);
//        accountRepository.save(fromAccount);
//
//        //saving transaction in toaccount
//        toAccount.getBankTransactions().add(savedTransaction);
//        accountRepository.save(toAccount);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        objectMapper.convertValue(savedTransaction, TransactionResponse.class)
                );
    }


    @Override
    public ResponseEntity<TransactionResponse> findById(Long id) {
       BankTransaction bankTransaction = bankTransactionRepository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Bank transaction not found")
        );
        return ResponseEntity.status(HttpStatus.FOUND).body(
                objectMapper.convertValue(bankTransaction, TransactionResponse.class)
        );
    }

    @Override
    public ResponseEntity<TransactionResponse> findByTransactionId(String transactionId) {
        BankTransaction bankTransaction=bankTransactionRepository.findByTransactionId(transactionId).orElseThrow(
                ()->new TransactionNotFoundException("Bank transaction not found")
        );
        return ResponseEntity.status(HttpStatus.FOUND).body(
                objectMapper.convertValue(bankTransaction, TransactionResponse.class)
        );
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> findByAccountNumber(String accountNumber) {
      Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                ()->new AccountNumberDoesNotExists("Account number not found")
        );
      List<TransactionResponse> transactionResponses=new ArrayList<>();
      for(BankTransaction bankTransaction:account.getBankTransactions()){
          transactionResponses.add( objectMapper.convertValue(bankTransaction, TransactionResponse.class));
      }
        return ResponseEntity.status(HttpStatus.FOUND).body(transactionResponses);
    }




    private void validateAccount(Account account) {

        if (account.getStatus() != AccountStatus.CLOSED) {

            throw new AccountClosedException("Account " + account.getAccountNumber() + " is closed");
        }
        if(account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException("Account " + account.getAccountNumber() + " is blocked");
        }
    }




    private String generateTransactionId() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

}
