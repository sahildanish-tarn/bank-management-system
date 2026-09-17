package com.jsp.bank_management_system.dto.transactionDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsp.bank_management_system.entity.Account;
import com.jsp.bank_management_system.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private String description;
    private LocalDateTime createdAt;
    private Account account;
}
