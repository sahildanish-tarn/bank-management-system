package com.jsp.bank_management_system.service.imp;


import com.jsp.bank_management_system.dto.LoanPaymentDtos.LoanPaymentRequest;
import com.jsp.bank_management_system.dto.LoanPaymentDtos.LoanPaymentResponse;
import com.jsp.bank_management_system.entity.Loan;
import com.jsp.bank_management_system.entity.LoanPayment;
import com.jsp.bank_management_system.enums.LoanStatus;
import com.jsp.bank_management_system.exception.InvalidAmountException;
import com.jsp.bank_management_system.exception.LoanNotFoundException;
import com.jsp.bank_management_system.repository.LoanPaymentRepository;
import com.jsp.bank_management_system.repository.LoanRepository;
import com.jsp.bank_management_system.service.LoanPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanPaymentServiceImpl implements LoanPaymentService {

    private final LoanPaymentRepository loanPaymentRepository;
    private final LoanRepository loanRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ResponseEntity<LoanPaymentResponse> makePayment(LoanPaymentRequest request) {

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Payment amount must be greater than zero");
        }

        Loan loan = loanRepository
                .findByLoanNumber(request.getLoanNumber())
                .orElseThrow(
                        () -> new LoanNotFoundException("Loan not found with loan number: " + request.getLoanNumber()
                        )
                );

        if (loan.getStatus() != LoanStatus.APPROVED && loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalArgumentException("Payment can be made only for an active loan");
        }


        if (request.getAmount().compareTo(loan.getOutstandingAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount cannot be greater than outstanding amount");
        }

        BigDecimal remainingAmount = loan.getOutstandingAmount().subtract(request.getAmount());

        loan.setOutstandingAmount(remainingAmount);


        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.CLOSED);
        } else {
            loan.setStatus(LoanStatus.ACTIVE);
        }
        loanRepository.save(loan);


        LoanPayment payment = new LoanPayment();
        payment.setPaymentId(generatePaymentId());
        payment.setAmount(request.getAmount());
        payment.setRemainingAmount(remainingAmount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setLoan(loan);


        LoanPayment savedPayment = loanPaymentRepository.save(payment);

      LoanPaymentResponse loanPaymentResponse =objectMapper.convertValue(savedPayment, LoanPaymentResponse.class);

      loanPaymentResponse.setLoanId(loan.getLoanNumber());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanPaymentResponse);
    }


    @Override
    public ResponseEntity<List<LoanPaymentResponse>>
    findByLoanNumber(String loanNumber) {

        // Check loan exists
        Loan loan=loanRepository
                .findByLoanNumber(loanNumber)
                .orElseThrow(() ->
                        new LoanNotFoundException(
                                "Loan not found with loan number: "
                                        + loanNumber
                        )
                );

        List<LoanPaymentResponse> responses =
                loanPaymentRepository
                        .findByLoanLoanNumber(loanNumber)
                        .stream()
                        .map(payment -> {

                            LoanPaymentResponse response =
                                    objectMapper.convertValue(
                                            payment,
                                            LoanPaymentResponse.class
                                    );

                            response.setLoanId(loan.getLoanNumber());

                            return response;
                        })
                        .toList();

        return ResponseEntity.ok(responses);
    }


    private String generatePaymentId() {

        return "PAY-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}