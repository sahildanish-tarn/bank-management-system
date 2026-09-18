package com.jsp.bank_management_system.dto.loanDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.enums.LoanStatus;
import com.jsp.bank_management_system.enums.LoanType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanRequest {
    private LoanType loanType;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer tenure;
    private BigDecimal outstandingAmount;

    private String customerId;
}
