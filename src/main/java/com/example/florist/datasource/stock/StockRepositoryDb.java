package com.example.florist.datasource.stock;

import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.domain.stock.Stock;
import com.example.florist.domain.stock.StockLot;
import com.example.florist.service.stock.StockRepository;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockRepositoryDb implements StockRepository {
    private final StockTableMapper stockTableMapper;

    @Override
    public Stock findStock() {
        return stockTableMapper.findAllStock();
    }

    @Override
    public List<StockLot> findAll() {
        return List.ofAll(stockTableMapper.findAllStock().lots());
    }

    @Override
    public void persist(FlowerOrderDetail detail) {
        stockTableMapper.insertArrival(detail);
    }

    @Override
    public void insertConsumption(ReceiptOrderDetail detail) {
        stockTableMapper.saveAllocations(detail);
    }
}
