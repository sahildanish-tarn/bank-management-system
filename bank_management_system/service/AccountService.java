package com.jsp.bank_management_system.service;



import com.jsp.bank_management_system.dto.accountDtos.AccountRequestDto;
import com.jsp.bank_management_system.dto.accountDtos.AccountResponseDto;
import com.jsp.bank_management_system.enums.AccountStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    ResponseEntity<AccountResponseDto> createAccount(AccountRequestDto requestDto);

    ResponseEntity<AccountResponseDto> findById(Long id);

    ResponseEntity<AccountResponseDto> findByAccountNumber(String accountNumber);

    ResponseEntity<List<AccountResponseDto>> findByCustomerId(String customerId);

    ResponseEntity<List<AccountResponseDto>> findAll();

    ResponseEntity<AccountResponseDto> updateStatus(String accountNumber, AccountStatus status);

    ResponseEntity<AccountResponseDto> closeAccount(String accountNumber);
}
