package com.example.florist.domain.flower;

import com.example.florist.domain.shared.DomainError;
import com.example.florist.domain.shared.Quantity;
import io.vavr.control.Either;

import java.time.LocalDate;

public record FlowerOrderDetail(
        Flower flower,
        Quantity quantity,
        LocalDate orderDate,
        LocalDate arrivalDate
) {
    public static Either<DomainError, FlowerOrderDetail> create(Flower flower, Quantity quantity, LocalDate orderDate) {
        if (!quantity.isGreaterThanOrEqual(flower.getMinUnitQuantity())) {
            return Either.left(new DomainError.ValidationError(
                    "quantity",
                    "Ordered quantity " + quantity.value() + " is lower than minimum unit quantity " + flower.getMinUnitQuantity().value() + " of " + flower.getName()
            ));
        }
        LocalDate arrivalDate = flower.calculateArrivalDate(orderDate);
        return Either.right(new FlowerOrderDetail(flower, quantity, orderDate, arrivalDate));
    }
}
