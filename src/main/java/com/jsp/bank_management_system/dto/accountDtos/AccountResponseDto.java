package com.jsp.bank_management_system.dto.accountDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsp.bank_management_system.entity.BankTransaction;
import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.enums.AccountStatus;
import com.jsp.bank_management_system.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status ;

}
