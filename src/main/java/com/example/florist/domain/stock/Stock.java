package com.example.florist.domain.stock;

import com.example.florist.domain.flower.Bouquet;
import com.example.florist.domain.flower.Flower;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.domain.shared.Quantity;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import io.vavr.control.Either;

import java.time.LocalDate;
import java.util.Comparator;

public record Stock(Vector<StockLot> lots) {

    public static Stock empty() {
        return new Stock(Vector.empty());
    }

    public Stock addLot(StockLot lot) {
        return new Stock(lots.append(lot));
    }

    public Vector<StockLot> freshLots(Flower flower, LocalDate deliveryDate) {
        return lots.filter(lot -> lot.flower().equals(flower) && lot.isFreshAt(deliveryDate) && lot.hasStock())
                .sorted(Comparator.comparing(StockLot::arrivalDate));
    }

    public Quantity totalFreshQuantity(Flower flower, LocalDate deliveryDate) {
        return freshLots(flower, deliveryDate)
                .map(StockLot::remainingQuantity)
                .foldLeft(Quantity.ZERO, Quantity::add);
    }

    public Either<DomainError.OutOfStockError, Tuple2<Stock, Vector<StockAllocation>>> allocate(
            Bouquet bouquet,
            LocalDate deliveryDate
    ) {
        for (Tuple2<Flower, Quantity> item : bouquet.getFlowerList()) {
            Flower flower = item._1;
            Quantity required = item._2;
            Quantity available = totalFreshQuantity(flower, deliveryDate);
            if (!available.isGreaterThanOrEqual(required)) {
                return Either.left(new DomainError.OutOfStockError(flower, required, available));
            }
        }

        Stock currentStock = this;
        Vector<StockAllocation> allAllocations = Vector.empty();

        for (Tuple2<Flower, Quantity> item : bouquet.getFlowerList()) {
            Flower flower = item._1;
            Quantity remainingDemand = item._2;

            Tuple2<Stock, Vector<StockAllocation>> allocationResult = currentStock.allocateFlower(
                    flower,
                    remainingDemand,
                    deliveryDate
            );
            currentStock = allocationResult._1;
            allAllocations = allAllocations.appendAll(allocationResult._2);
        }

        return Either.right(Tuple.of(currentStock, allAllocations));
    }

    private Tuple2<Stock, Vector<StockAllocation>> allocateFlower(
            Flower flower,
            Quantity demand,
            LocalDate deliveryDate
    ) {
        Quantity currentDemand = demand;
        Vector<StockAllocation> allocations = Vector.empty();
        Vector<StockLot> updatedLots = Vector.empty();

        for (StockLot lot : lots) {
            if (lot.flower().equals(flower) && lot.isFreshAt(deliveryDate) && lot.hasStock() && currentDemand.isPositive()) {
                Tuple2<StockLot, Quantity> allocatedTuple = lot.allocate(currentDemand);
                StockLot modifiedLot = allocatedTuple._1;
                Quantity allocatedQty = allocatedTuple._2;

                if (allocatedQty.isPositive()) {
                    allocations = allocations.append(new StockAllocation(flower, lot.arrivalDate(), allocatedQty));
                    currentDemand = currentDemand.subtract(allocatedQty);
                }
                updatedLots = updatedLots.append(modifiedLot);
            } else {
                updatedLots = updatedLots.append(lot);
            }
        }

        return Tuple.of(new Stock(updatedLots), allocations);
    }
}
