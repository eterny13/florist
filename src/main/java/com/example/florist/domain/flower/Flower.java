package com.example.florist.domain.flower;

import com.example.florist.domain.shared.Days;
import com.example.florist.domain.shared.Quantity;
import lombok.Value;

import java.time.LocalDate;

@Value
public class Flower {
    FlowerCode code;
    FlowerName name;
    Quantity minUnitQuantity;
    Days orderLeadTime;
    Days daysOfBestQuality;

    public Flower(FlowerCode code, FlowerName name, Quantity minUnitQuantity,
                  Days orderLeadTime, Days daysOfBestQuality) {
        this.code = code;
        this.name = name;
        this.minUnitQuantity = minUnitQuantity;
        this.orderLeadTime = orderLeadTime;
        this.daysOfBestQuality = daysOfBestQuality;
    }

    /** Convenience factory for callers that have primitive values (for example, fixtures). */
    public static Flower of(int code, String name, int minUnitQuantity, int orderLeadTime, int daysOfBestQuality) {
        return new Flower(
                new FlowerCode(code),
                new FlowerName(name),
                new Quantity(minUnitQuantity),
                new Days(orderLeadTime),
                new Days(daysOfBestQuality)
        );
    }

    /** Convenience constructor retained for primitive-valued callers. */
    public Flower(int code, String name, int minUnitQuantity, int orderLeadTime, int daysOfBestQuality) {
        this(new FlowerCode(code), new FlowerName(name), new Quantity(minUnitQuantity),
                new Days(orderLeadTime), new Days(daysOfBestQuality));
    }

    public LocalDate calculateArrivalDate(LocalDate orderDate) {
        return orderDate.plusDays(orderLeadTime.value());
    }

    public LocalDate calculateExpirationDate(LocalDate arrivalDate) {
        return arrivalDate.plusDays(daysOfBestQuality.value());
    }

    public boolean isFreshAt(LocalDate arrivalDate, LocalDate date) {
        return !date.isBefore(arrivalDate) && !date.isAfter(calculateExpirationDate(arrivalDate));
    }
}
