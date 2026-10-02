package com.example.florist.domain.customer;

public record CustomerName(String value) {
    public CustomerName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Customer name must not be blank");
        }
    }
}
