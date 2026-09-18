package com.jsp.bank_management_system.dto.transactionDtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    private String fromAccountNumber;

    private String toAccountNumber;
    private BigDecimal amount;
}
