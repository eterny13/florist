package com.example.florist.domain.shared;

import com.example.florist.domain.flower.Flower;

public sealed interface DomainError {
    record ValidationError(String field, String message) implements DomainError {
    }

    record NotFoundError(String message) implements DomainError {
    }

    record OutOfStockError(Flower flower, Quantity requested, Quantity available) implements DomainError {
    }

    record BusinessRuleViolation(String message) implements DomainError {
    }
}
