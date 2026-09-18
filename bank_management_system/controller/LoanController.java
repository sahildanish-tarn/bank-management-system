package com.jsp.bank_management_system.controller;

import com.jsp.bank_management_system.dto.loanDtos.LoanRequest;
import com.jsp.bank_management_system.dto.loanDtos.LoanResponse;
import com.jsp.bank_management_system.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;


    @PostMapping
    public ResponseEntity<LoanResponse> applyLoan(
            @RequestBody @Valid LoanRequest request) {

        return loanService.applyLoan(request);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> findById(
            @PathVariable Long id) {

        return loanService.findById(id);
    }


    @GetMapping("/number/{loanNumber}")
    public ResponseEntity<LoanResponse> findByLoanNumber(
            @PathVariable String loanNumber) {

        return loanService.findByLoanNumber(loanNumber);
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanResponse>> findByCustomerId(
            @PathVariable String customerId) {

        return loanService.findByCustomerId(customerId);
    }


    @GetMapping
    public ResponseEntity<List<LoanResponse>> findAll() {

        return loanService.findAll();
    }


    @PatchMapping("/{loanNumber}/approve")
    public ResponseEntity<LoanResponse> approveLoan(
            @PathVariable String loanNumber) {

        return loanService.approveLoan(loanNumber);
    }


    @PatchMapping("/{loanNumber}/reject")
    public ResponseEntity<LoanResponse> rejectLoan(
            @PathVariable String loanNumber) {

        return loanService.rejectLoan(loanNumber);
    }


    @PatchMapping("/{loanNumber}/close")
    public ResponseEntity<LoanResponse> closeLoan(
            @PathVariable String loanNumber) {

        return loanService.closeLoan(loanNumber);
    }
}