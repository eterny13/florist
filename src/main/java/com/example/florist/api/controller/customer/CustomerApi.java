package com.example.florist.api.controller.customer;

import com.example.florist.api.controller.customer.request.CustomerRequest;
import com.example.florist.api.controller.general.DomainException;
import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.service.customer.CustomerService;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerApi {
    private static final URI CUSTOMERS_URI = URI.create("/customers");
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody CustomerRequest request) {
        Either<Seq<DomainError>, Customer> result = service.register(request.name(), request.email());

        return result.fold(
                errors -> { throw new DomainException(errors, CUSTOMERS_URI); },
                customer -> ResponseEntity.status(HttpStatus.CREATED).body("Customer ID: " + customer.getId().value())
        );
    }
}
