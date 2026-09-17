package com.jsp.bank_management_system.service;


import com.jsp.bank_management_system.dto.LoanPaymentDtos.LoanPaymentRequest;
import com.jsp.bank_management_system.dto.LoanPaymentDtos.LoanPaymentResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanPaymentService {

    ResponseEntity<LoanPaymentResponse> makePayment(LoanPaymentRequest request);

    ResponseEntity<List<LoanPaymentResponse>> findByLoanNumber(String loanNumber);
}