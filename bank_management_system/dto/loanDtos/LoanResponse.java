package com.jsp.bank_management_system.dto.loanDtos;

import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.enums.LoanStatus;
import com.jsp.bank_management_system.enums.LoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {
    private Long id;

    private String loanNumber;

    private LoanType loanType;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer tenure;
    private BigDecimal outstandingAmount;
    private LoanStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime approvedAt;
    private String customerId;
}
