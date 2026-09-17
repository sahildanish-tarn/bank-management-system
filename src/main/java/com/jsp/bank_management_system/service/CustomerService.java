package com.jsp.bank_management_system.service;

import com.jsp.bank_management_system.dto.customerDtos.CustomerRequest;
import com.jsp.bank_management_system.dto.customerDtos.CustomerResponse;
import com.jsp.bank_management_system.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {

    ResponseEntity<CustomerResponse> createCustomer(CustomerRequest request);

    ResponseEntity<CustomerResponse> getCustomer(Long id);

    ResponseEntity<List<CustomerResponse>> getAllCustomers();

    ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);

    ResponseEntity<CustomerResponse> getByEmail(String email);
}