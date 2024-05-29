package com.example.florist.service.stock;

import com.example.florist.domain.receipt_order.ReceiptOrderDetail;

public interface StockRepository {
    boolean confirm(ReceiptOrderDetail detail);

    void persist(ReceiptOrderDetail detail);
}
