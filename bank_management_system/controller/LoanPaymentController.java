package com.jsp.bank_management_system.controller;

import com.jsp.bank_management_system.dto.LoanPaymentDtos.LoanPaymentRequest;
import com.jsp.bank_management_system.dto.LoanPaymentDtos.LoanPaymentResponse;
import com.jsp.bank_management_system.service.LoanPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-payment")
@RequiredArgsConstructor
public class LoanPaymentController {

    private final LoanPaymentService loanPaymentService;

    // Make loan payment
    @PostMapping
    public ResponseEntity<LoanPaymentResponse> makePayment(@RequestBody @Valid LoanPaymentRequest request) {

        return loanPaymentService.makePayment(request);
    }


    // Find all payments for a loan
    @GetMapping("/loan/{loanNumber}")
    public ResponseEntity<List<LoanPaymentResponse>> findByLoanNumber(
            @PathVariable String loanNumber) {

        return loanPaymentService.findByLoanNumber(loanNumber);
    }
}
