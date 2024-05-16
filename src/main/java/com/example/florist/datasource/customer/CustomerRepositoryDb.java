package com.example.florist.datasource.customer;

import com.example.florist.domain.customer.Customer;
import com.example.florist.service.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerRepositoryDb implements CustomerRepository {
    private final CustomerTableMapper customerTableMapper;

    @Override
    public void persist(Customer customer) {
        customerTableMapper.persist(customer);
    }
}
