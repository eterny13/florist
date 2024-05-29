package com.example.florist.datasource.receipt_order;

import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.service.receipt_order.ReceiptOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiptOrderRepositoryDb implements ReceiptOrderRepository {
    private final ReceiptOrderTableMapper receiptOrderTableMapper;

    @Override
    public void persist(ReceiptOrderDetail receiptOrderDetail) {
        receiptOrderTableMapper.insert(receiptOrderDetail);
    }
}
