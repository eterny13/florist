package com.example.florist.domain.customer;

import java.util.UUID;

public record CustomerId(String value) {
    public CustomerId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CustomerId must not be blank");
        }
    }

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID().toString().split("-")[0]);
    }
}
