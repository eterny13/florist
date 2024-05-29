package com.example.florist.domain.customer;

import lombok.Value;

import java.util.UUID;

@Value
public class Customer {
    String id;
    String name;
    String email;

    public Customer(String name, String email) {
        this.id = UUID.randomUUID().toString().split("-")[0];
        this.name = name;
        this.email = email;
    }

    public Customer(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
