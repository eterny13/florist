package com.example.florist.domain.flower;

import io.vavr.control.Option;
import lombok.Value;

import java.time.LocalDate;

@Value
public class FlowerOrderDetail {
    Flower flower;
    int quantity;
    LocalDate arrivalDate;

    public FlowerOrderDetail(Option<Flower> flower, int quantity) {
        if (quantity < flower.get().getMinUnitQuantity()) {
            throw new RuntimeException("Ordered quantity is lower than minimum unit quantity of the flower.");
        }
        this.flower = flower.getOrElseThrow(() -> new RuntimeException("Undefined flower code"));
        this.quantity = quantity;
        this.arrivalDate = LocalDate.now().plusDays(this.flower.getOrderLeadTime());
    }
}
