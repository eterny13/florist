package com.example.florist.datasource.stock;

import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.service.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockRepositoryDb implements StockRepository {
    private final StockTableMapper stockTableMapper;

    @Override
    public boolean confirm(ReceiptOrderDetail detail) {
        return stockTableMapper.confirm(detail);
    }

    @Override
    public void persist(ReceiptOrderDetail detail) {
        stockTableMapper.insert(detail);
    }
}
