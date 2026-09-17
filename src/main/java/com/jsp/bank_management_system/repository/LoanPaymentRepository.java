package com.jsp.bank_management_system.repository;

import com.jsp.bank_management_system.entity.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment,Long> {
    List<LoanPayment> findByLoanLoanNumber(String loanNumber);
}
