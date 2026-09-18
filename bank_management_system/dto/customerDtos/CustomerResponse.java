package com.jsp.bank_management_system.dto.customerDtos;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private String customerId;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String pincode;
}
