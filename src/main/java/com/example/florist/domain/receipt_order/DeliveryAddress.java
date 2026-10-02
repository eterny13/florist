package com.example.florist.domain.receipt_order;

public record DeliveryAddress(String value) {
    public DeliveryAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Delivery address must not be blank");
        }
    }
}
