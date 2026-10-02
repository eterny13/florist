package com.example.florist.service.customer;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.customer.CustomerId;
import io.vavr.control.Option;

public interface CustomerRepository {
    void persist(Customer customer);

    Option<Customer> findById(CustomerId customerId);

    default Option<Customer> findById(String customerId) {
        return findById(new CustomerId(customerId));
    }

    default Customer get(String customerId) {
        return findById(customerId).getOrElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
    }
}
