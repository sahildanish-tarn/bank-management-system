package com.jsp.bank_management_system.controller;


import com.jsp.bank_management_system.dto.customerDtos.CustomerRequest;
import com.jsp.bank_management_system.dto.customerDtos.CustomerResponse;
import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping("/create")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
           return   customerService.createCustomer(request);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
             return    customerService.getCustomer(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
             return    customerService.getAllCustomers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest request) {
             return    customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.ok(
                "Customer deleted successfully"
        );
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getByEmail(@RequestHeader String email) {


           return  customerService.getByEmail(email);

    }
}
