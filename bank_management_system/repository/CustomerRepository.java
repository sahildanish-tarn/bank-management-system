package com.jsp.bank_management_system.repository;

import com.jsp.bank_management_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByCustomerId(String customerId);
    boolean existsByEmail(String email);
    boolean existsByCustomerId(String customerId);
}
