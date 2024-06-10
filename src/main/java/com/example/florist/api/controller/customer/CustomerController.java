package com.example.florist.api.controller.customer;

import com.example.florist.api.controller.customer.request.CustomerRequest;
import com.example.florist.domain.customer.Customer;
import com.example.florist.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private static final String value = "/customers";
    private final CustomerService service;

    @RequestMapping(value = value, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> post(@RequestBody CustomerRequest request) {
        if (Objects.equals(request.getName(), "")) {
            var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setType(URI.create(value));
            problemDetail.setDetail("Validation Error: Invalid Name");
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null);
        }
        if (!request.getEmail().contains("@")) {
            var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setType(URI.create(value));
            problemDetail.setDetail("Validation Error: Invalid Email");
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null);
        }

        var customer = new Customer(request.getName(), request.getEmail());
        service.persist(customer);
        return ResponseEntity.ok("Customer ID: " + customer.getId());
    }
}
