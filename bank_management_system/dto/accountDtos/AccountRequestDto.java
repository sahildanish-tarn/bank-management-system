package com.jsp.bank_management_system.dto.accountDtos;

import com.jsp.bank_management_system.enums.AccountStatus;
import com.jsp.bank_management_system.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class AccountRequestDto {
    private AccountType accountType;
    private BigDecimal balance ;
    private String customerId;
}
