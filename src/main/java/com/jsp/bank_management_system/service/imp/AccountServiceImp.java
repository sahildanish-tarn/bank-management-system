package com.jsp.bank_management_system.service.imp;

import com.jsp.bank_management_system.exception.AccountAlreadyClosedException;
import com.jsp.bank_management_system.dto.accountDtos.AccountRequestDto;
import com.jsp.bank_management_system.dto.accountDtos.AccountResponseDto;
import com.jsp.bank_management_system.entity.Account;
import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.enums.AccountStatus;
import com.jsp.bank_management_system.exception.AccountNumberDoesNotExists;
import com.jsp.bank_management_system.exception.CustomerNotFoundException;
import com.jsp.bank_management_system.repository.AccountRepository;
import com.jsp.bank_management_system.repository.CustomerRepository;
import com.jsp.bank_management_system.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<AccountResponseDto> createAccount(AccountRequestDto requestDto) {
       Customer customer =customerRepository.findByCustomerId(requestDto.getCustomerId()).orElseThrow(
                ()-> new CustomerNotFoundException("Customer Not Found")
        );
       Account account =new Account();
       account.setCustomer(customer);
       account.setBalance(requestDto.getBalance());
       account.setAccountType(requestDto.getAccountType());
       account.setStatus(AccountStatus.ACTIVE);
       account.setAccountNumber(generateAccountNumber());

       Account dbAccountObj =accountRepository.save(account);

       customer.getAccounts().add(account);
       customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.convertValue(dbAccountObj,AccountResponseDto.class));
    }

    @Override
    public ResponseEntity<AccountResponseDto> findById(Long id) {
        Account account=accountRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Account Not Found")
        );

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(objectMapper.convertValue(account,AccountResponseDto.class));
    }

    @Override
    public  ResponseEntity<AccountResponseDto> findByAccountNumber(String accountNumber) {
        Account account=accountRepository.findByAccountNumber(accountNumber).
                orElseThrow(()-> new RuntimeException("Account Not Found"));
        return ResponseEntity.status(HttpStatus.FOUND).body(
                objectMapper.convertValue(account,AccountResponseDto.class)
        );
    }

    @Override
    public ResponseEntity<List<AccountResponseDto>> findByCustomerId(String customerId) {
        Customer customer =customerRepository.findByCustomerId(customerId).orElseThrow(
                ()-> new CustomerNotFoundException("Customer with given customer id does not exist")
        );

        List<AccountResponseDto> dtos=new ArrayList<>();
        for (Account account:customer.getAccounts()) {
            dtos.add(objectMapper.convertValue(account,AccountResponseDto.class));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(dtos);

    }

    @Override
    public ResponseEntity<List<AccountResponseDto>> findAll() {

        List<AccountResponseDto> dtos=new ArrayList<>();
        for (Account account:accountRepository.findAll()) {
            dtos.add(objectMapper.convertValue(account,AccountResponseDto.class));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(dtos);
    }

    @Override
    public ResponseEntity<AccountResponseDto> updateStatus(String accountNumber, AccountStatus status) {
        Account account =accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                ()-> new AccountNumberDoesNotExists("Account Not Found")
        );
        if(account.getStatus()==AccountStatus.CLOSED){
            throw new RuntimeException("Account does not exist");
        }
        account.setStatus(status);
        Account dbAccount =accountRepository.save(account);
        return ResponseEntity.ok(
                objectMapper.convertValue(dbAccount,AccountResponseDto.class)
        );
    }

    //soft delete
    @Override
    public ResponseEntity<AccountResponseDto> closeAccount(String accountNumber) {
        Account account =accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                ()-> new AccountNumberDoesNotExists("Account Not Found")
        );

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new AccountAlreadyClosedException("Account is already closed");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO)<=0) {
            throw new RuntimeException("Account cannot be closed because balance is not zero");
        }

        account.setStatus(AccountStatus.CLOSED);
        Account dbAccount =accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        objectMapper.convertValue(dbAccount,AccountResponseDto.class)
                );
    }

    public String generateAccountNumber(){

        return String.valueOf(System.currentTimeMillis());
    }
}
