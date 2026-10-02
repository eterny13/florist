package com.example.florist.domain.flower;

public record FlowerName(String value) {
    public FlowerName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Flower name must not be blank");
        }
    }
}
