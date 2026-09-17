package com.jsp.bank_management_system.controller;

import com.jsp.bank_management_system.dto.accountDtos.AccountRequestDto;
import com.jsp.bank_management_system.dto.accountDtos.AccountResponseDto;
import com.jsp.bank_management_system.enums.AccountStatus;
import com.jsp.bank_management_system.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class Accountcontroller {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody @Valid AccountRequestDto requestDto) {
        return accountService.createAccount(requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> findById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @GetMapping
    public ResponseEntity<AccountResponseDto> findByAccountNumber(@RequestHeader String accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }

   @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDto>> findByCustomerId(@PathVariable String customerId) {
        return accountService.findByCustomerId(customerId);
    }

   @GetMapping("/all")
    public ResponseEntity<List<AccountResponseDto>> findAll() {
        return accountService.findAll();
    }


    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<AccountResponseDto> updateStatus(@PathVariable String accountNumber,@RequestParam AccountStatus status) {
        return accountService.updateStatus(accountNumber,status);
    }


    @DeleteMapping("/close/{accountNumber}")
    public ResponseEntity<AccountResponseDto> closeAccount(@PathVariable String accountNumber) {
        return accountService.closeAccount(accountNumber);
    }
}
