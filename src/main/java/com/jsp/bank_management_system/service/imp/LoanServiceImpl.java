package com.jsp.bank_management_system.service.imp;

import com.jsp.bank_management_system.dto.loanDtos.LoanRequest;
import com.jsp.bank_management_system.dto.loanDtos.LoanResponse;
import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.entity.Loan;
import com.jsp.bank_management_system.enums.LoanStatus;
import com.jsp.bank_management_system.exception.CustomerNotFoundException;
import com.jsp.bank_management_system.exception.LoanNotFoundException;
import com.jsp.bank_management_system.repository.CustomerRepository;
import com.jsp.bank_management_system.repository.LoanRepository;
import com.jsp.bank_management_system.service.LoanService;
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
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;


    // =========================
    // APPLY LOAN
    // =========================

    @Override
    @Transactional
    public ResponseEntity<LoanResponse> applyLoan(
            LoanRequest request) {

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Loan amount must be greater than zero"
            );
        }

        Customer customer = customerRepository
                .findByCustomerId(request.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with customer ID: "
                                        + request.getCustomerId()
                        ));

        Loan loan = new Loan();

        loan.setLoanNumber(generateLoanNumber());
        loan.setLoanType(request.getLoanType());
        loan.setAmount(request.getAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTenure(request.getTenure());

        // Initially outstanding amount is the loan amount
        loan.setOutstandingAmount(request.getAmount());

        loan.setStatus(LoanStatus.PENDING);

        loan.setAppliedAt(LocalDateTime.now());

        loan.setCustomer(customer);

        Loan savedLoan = loanRepository.save(loan);

        LoanResponse response =
                objectMapper.convertValue(
                        savedLoan,
                        LoanResponse.class
                );
        response.setCustomerId(customer.getCustomerId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================
    // FIND BY ID
    // =========================

    @Override
    public ResponseEntity<LoanResponse> findById(
            Long id) {

        Loan loan = loanRepository
                .findById(id)
                .orElseThrow(() ->
                        new LoanNotFoundException(
                                "Loan not found with ID: " + id
                        ));

        LoanResponse response =
                objectMapper.convertValue(
                        loan,
                        LoanResponse.class
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // FIND BY LOAN NUMBER
    // =========================

    @Override
    public ResponseEntity<LoanResponse> findByLoanNumber(
            String loanNumber) {

        Loan loan = loanRepository
                .findByLoanNumber(loanNumber)
                .orElseThrow(() ->
                        new LoanNotFoundException(
                                "Loan not found with loan number: "
                                        + loanNumber
                        ));

        LoanResponse response =
                objectMapper.convertValue(
                        loan,
                        LoanResponse.class
                );
        response.setCustomerId(loan.getCustomer().getCustomerId());
        return ResponseEntity.ok(response);
    }


    // =========================
    // FIND CUSTOMER LOANS
    // =========================

    @Override
    public ResponseEntity<List<LoanResponse>> findByCustomerId(String customerId) {

        if (!customerRepository.existsByCustomerId(customerId)) {

            throw new CustomerNotFoundException(
                    "Customer not found with customer ID: "
                            + customerId
            );
        }

        List<LoanResponse> responses =
                loanRepository.findByCustomer_CustomerId(customerId)
                        .stream()
                        .map(loan -> {
                                    LoanResponse response = objectMapper.convertValue(
                                            loan,
                                            LoanResponse.class
                                    );
                                    response.setCustomerId(customerId);
                                    return response;
                                }
                        )
                        .toList();
        return ResponseEntity.ok(responses);
    }


    // =========================
    // FIND ALL
    // =========================

    @Override
    public ResponseEntity<List<LoanResponse>> findAll() {


        List<LoanResponse> responses =
                loanRepository.findAll()
                        .stream()
                        .map(loan -> {


                                    LoanResponse response = objectMapper.convertValue(
                                            loan,
                                            LoanResponse.class
                                    );
                                    response.setCustomerId(loan.getCustomer().getCustomerId());

                                    return response;
                                }
                        )
                        .toList();

        return ResponseEntity.ok(responses);
    }


    // =========================
    // APPROVE LOAN
    // =========================

    @Override
    @Transactional
    public ResponseEntity<LoanResponse> approveLoan(
            String loanNumber) {

        Loan loan = getLoan(loanNumber);

        if (loan.getStatus() != LoanStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Only pending loans can be approved"
            );
        }

        loan.setStatus(LoanStatus.APPROVED);
//        loan.setStatus(LoanStatus.ACTIVE);
        loan.setApprovedAt(LocalDateTime.now());

        Loan updatedLoan = loanRepository.save(loan);

        LoanResponse response =
                objectMapper.convertValue(
                        updatedLoan,
                        LoanResponse.class
                );
        response.setCustomerId(updatedLoan.getCustomer().getCustomerId());

        return ResponseEntity.ok(response);
    }


    // =========================
    // REJECT LOAN
    // =========================

    @Override
    @Transactional
    public ResponseEntity<LoanResponse> rejectLoan(
            String loanNumber) {

        Loan loan = getLoan(loanNumber);

        if (loan.getStatus() != LoanStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Only pending loans can be rejected"
            );
        }

        loan.setStatus(LoanStatus.REJECTED);

        Loan updatedLoan = loanRepository.save(loan);

        LoanResponse response =
                objectMapper.convertValue(
                        updatedLoan,
                        LoanResponse.class
                );
        response.setCustomerId(updatedLoan.getCustomer().getCustomerId());

        return ResponseEntity.ok(response);
    }


    // =========================
    // CLOSE LOAN
    // =========================

    @Override
    @Transactional
    public ResponseEntity<LoanResponse> closeLoan(
            String loanNumber) {

        Loan loan = getLoan(loanNumber);

        if (loan.getStatus() != LoanStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Only active loans can be closed"
            );
        }

        if (loan.getOutstandingAmount()
                .compareTo(BigDecimal.ZERO) > 0) {

            throw new IllegalArgumentException(
                    "Loan cannot be closed because outstanding amount is not zero"
            );
        }

        loan.setStatus(LoanStatus.CLOSED);

        Loan updatedLoan = loanRepository.save(loan);

        LoanResponse response =
                objectMapper.convertValue(
                        updatedLoan,
                        LoanResponse.class
                );

        response.setCustomerId(updatedLoan.getCustomer().getCustomerId());
        return ResponseEntity.ok(response);
    }


    // =========================
    // HELPER METHOD
    // =========================

    private Loan getLoan(String loanNumber) {

        return loanRepository
                .findByLoanNumber(loanNumber)
                .orElseThrow(() ->
                        new LoanNotFoundException(
                                "Loan not found with loan number: "
                                        + loanNumber
                        ));
    }


    // =========================
    // GENERATE LOAN NUMBER
    // =========================

    private String generateLoanNumber() {

        return "LN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}