package com.example.florist.service.receipt_order;

import com.example.florist.domain.receipt_order.ReceiptOrderDetailFactory;
import com.example.florist.service.customer.CustomerRepository;
import com.example.florist.service.flower_order.AvailableFlowerRepository;
import com.example.florist.service.stock.StockRepository;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptOrderService {
    private final AvailableFlowerRepository availableFlowerRepository;
    private final CustomerRepository customerRepository;
    private final ReceiptOrderRepository receiptOrderRepository;
    private final StockRepository stockRepository;

    public void receive(
            String customerId,
            String deliveryDateStr,
            String deliveryAddress,
            String recipientName,
            int bouquetId,
            Option<String> deliveryMessage,
            String recipientPhoneNumber) {

        var customer = customerRepository.get(customerId);
        var bouquets = availableFlowerRepository.referBouquets();
        var receiptOrderDetail = ReceiptOrderDetailFactory.create(customer, deliveryDateStr, deliveryAddress, recipientName, bouquetId, deliveryMessage, recipientPhoneNumber, bouquets);

        var isPossibleToOrder = stockRepository.confirm(receiptOrderDetail);
        if (isPossibleToOrder) {
            receiptOrderRepository.persist(receiptOrderDetail);
            stockRepository.persist(receiptOrderDetail);
        } else throw new RuntimeException("Out of the Flower Stock");
    }
}
