package com.example.florist.service.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;

import java.time.LocalDate;

public interface FlowerOrderRepository {
    void persist(FlowerOrderDetail flowerOrderDetail);

    void persist(ReceiptOrderDetail receiptOrderDetail, LocalDate deliveryDate);
}
