package com.example.florist.service.stock;

import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.domain.stock.Stock;
import com.example.florist.domain.stock.StockLot;
import io.vavr.collection.List;

public interface StockRepository {
    Stock findStock();

    List<StockLot> findAll();

    void persist(FlowerOrderDetail detail);

    void insertConsumption(ReceiptOrderDetail detail);

    default Stock findAllStock() {
        return findStock();
    }

    default void insertArrival(FlowerOrderDetail detail) {
        persist(detail);
    }

    default void saveAllocations(ReceiptOrderDetail detail) {
        insertConsumption(detail);
    }
}
