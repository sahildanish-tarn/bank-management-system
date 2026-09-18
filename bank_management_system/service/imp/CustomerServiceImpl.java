package com.jsp.bank_management_system.service.imp;


import com.jsp.bank_management_system.dto.customerDtos.CustomerRequest;
import com.jsp.bank_management_system.dto.customerDtos.CustomerResponse;
import com.jsp.bank_management_system.entity.Customer;
import com.jsp.bank_management_system.entity.User;
import com.jsp.bank_management_system.exception.CustomerNotFoundException;
import com.jsp.bank_management_system.exception.UserNotfound;
import com.jsp.bank_management_system.repository.CustomerRepository;
import com.jsp.bank_management_system.repository.UserRepository;
import com.jsp.bank_management_system.service.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UserNotfound("Invalid username")
                );

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPincode(request.getPincode());
        customer.setCustomerId(generateCustomerId());

        customer.setUser(user);
        customer.setDeleted(false);

        Customer dbCustomer = customerRepository.save(customer);

        user.setCustomer(dbCustomer);
        userRepository.save(user);

        CustomerResponse response =
                objectMapper.convertValue(dbCustomer, CustomerResponse.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomer(Long id) {
       Customer customer= customerRepository.findById(id).orElseThrow(
                ()-> new CustomerNotFoundException("Customer not found with id " + id)
        );
        if(customer.isDeleted()){
            throw new CustomerNotFoundException("Customer not found");
        }
        return ResponseEntity.status(HttpStatus.FOUND).
                body(objectMapper.convertValue(customer,CustomerResponse.class));

    }

    @Override
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
       List<CustomerResponse> customerResponses=new ArrayList<>();
        for (Customer customer : customerRepository.findAll()) {
            customerResponses.add(objectMapper.convertValue(customer,CustomerResponse.class));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(customerResponses);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                 () -> new CustomerNotFoundException("Customer not found")
         );
        if(customer.isDeleted()){
            throw new CustomerNotFoundException("Customer not found");
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPincode(request.getPincode());

        Customer dbcustomer =customerRepository.save(customer);
        return ResponseEntity.ok(objectMapper.convertValue(dbcustomer,CustomerResponse.class));
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer not found")
        );

        if(customer.isDeleted()){
            throw new CustomerNotFoundException("Customer not found");
        }
        customer.setDeleted(true);

        customerRepository.save(customer);
    }

    @Override
    public ResponseEntity<CustomerResponse> getByEmail(String email) {

       Customer customer= customerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if(customer.isDeleted()){
            throw new CustomerNotFoundException("Customer not found");
        }
       return ResponseEntity.status(HttpStatus.FOUND).body( objectMapper.convertValue(customer,CustomerResponse.class));
    }

    private String generateCustomerId() {

        return "CUST-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}