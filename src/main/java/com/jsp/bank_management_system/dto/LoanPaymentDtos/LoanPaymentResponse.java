package com.jsp.bank_management_system.dto.LoanPaymentDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsp.bank_management_system.entity.Loan;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanPaymentResponse {

    private Long id;
    private String paymentId;
    private BigDecimal amount;
    private BigDecimal remainingAmount;
    private LocalDateTime paymentDate;
    private String LoanId;
}
