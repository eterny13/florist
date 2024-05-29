package com.example.florist.service.receipt_order;

import com.example.florist.domain.receipt_order.ReceiptOrderDetail;

public interface ReceiptOrderRepository {
    void persist(ReceiptOrderDetail receiptOrderDetail);
}
