package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.service.flower_order.FlowerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FlowerOrderRepositoryDb implements FlowerOrderRepository {
    private final FlowerOrderTableMapper flowerOrderTableMapper;

    @Override
    public void persist(FlowerOrderDetail detail) {
        flowerOrderTableMapper.insert(detail);
    }

    @Override
    public void persist(ReceiptOrderDetail receiptOrderDetail, LocalDate deliveryDate) {
        flowerOrderTableMapper.insert(receiptOrderDetail, deliveryDate);
    }
}
