package com.example.florist.service.customer;


import com.example.florist.domain.customer.Customer;

public interface CustomerRepository  {
    void persist(Customer customer);
}
