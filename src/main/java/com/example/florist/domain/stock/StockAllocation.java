package com.example.florist.domain.stock;

import com.example.florist.domain.flower.Flower;
import com.example.florist.domain.shared.Quantity;

import java.time.LocalDate;

public record StockAllocation(
        Flower flower,
        LocalDate arrivalDate,
        Quantity quantity
) {
}
