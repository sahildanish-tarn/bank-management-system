package com.jsp.bank_management_system.repository;

import com.jsp.bank_management_system.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Optional<Loan> findByLoanNumber(String loanNumber);

    List<Loan> findByCustomer_CustomerId(String customerId);
}
