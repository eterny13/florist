package com.example.florist.api.controller.customer;

import com.example.florist.api.controller.customer.request.CustomerRequest;
import com.example.florist.domain.customer.Customer;
import com.example.florist.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Created";
    }
}
