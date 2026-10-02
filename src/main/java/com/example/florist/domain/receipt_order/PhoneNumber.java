package com.example.florist.domain.receipt_order;

public record PhoneNumber(String value) {
    public PhoneNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Phone number must not be blank");
        }
    }
}
