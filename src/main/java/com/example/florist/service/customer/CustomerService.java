package com.example.florist.service.customer;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.customer.CustomerId;
import com.example.florist.domain.shared.DomainError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    @Transactional
    public Either<Seq<DomainError>, Customer> register(String name, String email) {
        Validation<Seq<DomainError>, Customer> validation = Customer.create(name, email);
        if (validation.isInvalid()) {
            return Either.left(validation.getError());
        }
        Customer customer = validation.get();
        repository.persist(customer);
        return Either.right(customer);
    }

    @Transactional
    public void persist(Customer customer) {
        repository.persist(customer);
    }

    public Option<Customer> findById(CustomerId id) {
        return repository.findById(id);
    }
}
