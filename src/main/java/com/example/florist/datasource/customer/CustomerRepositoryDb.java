package com.example.florist.datasource.customer;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.customer.CustomerId;
import com.example.florist.service.customer.CustomerRepository;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryDb implements CustomerRepository {
    private final CustomerTableMapper customerTableMapper;

    @Override
    public void persist(Customer customer) {
        customerTableMapper.insert(customer);
    }

    @Override
    public Option<Customer> findById(CustomerId customerId) {
        return customerTableMapper.findById(customerId.value());
    }
}
