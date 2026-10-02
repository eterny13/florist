package com.example.florist.domain.stock;

import com.example.florist.domain.flower.Flower;
import com.example.florist.domain.shared.Quantity;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.time.LocalDate;

public record StockLot(
        Flower flower,
        Quantity initialQuantity,
        Quantity remainingQuantity,
        LocalDate arrivalDate
) {
    public static StockLot of(Flower flower, int initialQuantity, int remainingQuantity, LocalDate arrivalDate) {
        return new StockLot(flower, new Quantity(initialQuantity), new Quantity(remainingQuantity), arrivalDate);
    }

    public static StockLot ofNewArrival(Flower flower, Quantity quantity, LocalDate arrivalDate) {
        return new StockLot(flower, quantity, quantity, arrivalDate);
    }

    public LocalDate expirationDate() {
        return flower.calculateExpirationDate(arrivalDate);
    }

    public boolean isFreshAt(LocalDate date) {
        return flower.isFreshAt(arrivalDate, date);
    }

    public boolean hasStock() {
        return remainingQuantity.isPositive();
    }

    public Tuple2<StockLot, Quantity> allocate(Quantity demand) {
        int allocated = Math.min(this.remainingQuantity.value(), demand.value());
        Quantity newRemaining = this.remainingQuantity.subtract(new Quantity(allocated));
        StockLot updatedLot = new StockLot(flower, initialQuantity, newRemaining, arrivalDate);
        return Tuple.of(updatedLot, new Quantity(allocated));
    }
}
