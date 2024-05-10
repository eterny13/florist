package com.example.florist.service.customer;

import com.example.florist.domain.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public void persist(Customer customer) {
        repository.persist(customer);
    }

}
