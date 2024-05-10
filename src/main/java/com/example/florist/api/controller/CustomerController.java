package com.example.florist.api.controller;

import com.example.florist.api.controller.http_model.customer.CustomerRequest;
import com.example.florist.domain.customer.Customer;
import com.example.florist.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody CustomerRequest request) {
        var customer = new Customer(request.name(), request.email());
        try {
            service.persist(customer);
            return "Created";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
