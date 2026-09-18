package com.jsp.bank_management_system.service;

import com.jsp.bank_management_system.dto.loanDtos.LoanRequest;
import com.jsp.bank_management_system.dto.loanDtos.LoanResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface LoanService {

    ResponseEntity<LoanResponse> applyLoan(LoanRequest request);

    ResponseEntity<LoanResponse> findById(Long id);

    ResponseEntity<LoanResponse> findByLoanNumber(String loanNumber);

    ResponseEntity<List<LoanResponse>> findByCustomerId(String customerId);

    ResponseEntity<List<LoanResponse>> findAll();

    ResponseEntity<LoanResponse> approveLoan(String loanNumber);

    ResponseEntity<LoanResponse> rejectLoan(String loanNumber);

    ResponseEntity<LoanResponse> closeLoan(String loanNumber);
}
