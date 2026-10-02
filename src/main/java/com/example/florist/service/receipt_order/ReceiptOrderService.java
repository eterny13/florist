package com.example.florist.service.receipt_order;

import com.example.florist.domain.customer.CustomerId;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetailFactory;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.domain.stock.Stock;
import com.example.florist.domain.stock.StockAllocation;
import com.example.florist.service.customer.CustomerRepository;
import com.example.florist.service.flower_order.AvailableFlowerRepository;
import com.example.florist.service.stock.StockRepository;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptOrderService {
    private final AvailableFlowerRepository availableFlowerRepository;
    private final CustomerRepository customerRepository;
    private final ReceiptOrderRepository receiptOrderRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Either<DomainError, ReceiptOrderDetail> receive(
            String customerId,
            String deliveryDateStr,
            String deliveryAddress,
            String recipientName,
            int bouquetId,
            Option<String> deliveryMessage,
            String recipientPhoneNumber
    ) {
        return customerRepository.findById(new CustomerId(customerId))
                .toEither(() -> (DomainError) new DomainError.NotFoundError("Customer not found: " + customerId))
                .flatMap(customer -> {
                    var bouquets = availableFlowerRepository.findAllBouquets();
                    return ReceiptOrderDetailFactory.create(
                            customer,
                            deliveryDateStr,
                            deliveryAddress,
                            recipientName,
                            bouquetId,
                            deliveryMessage,
                            recipientPhoneNumber,
                            bouquets
                    );
                })
                .flatMap(initialDetail -> {
                    Stock currentStock = stockRepository.findAllStock();
                    return currentStock.allocate(initialDetail.bouquet(), initialDetail.deliveryDate())
                            .mapLeft(error -> (DomainError) error)
                            .map(allocationResult -> {
                                Vector<StockAllocation> allocations = allocationResult._2;
                                ReceiptOrderDetail finalDetail = initialDetail.withAllocations(allocations);

                                receiptOrderRepository.persist(finalDetail);
                                stockRepository.saveAllocations(finalDetail);
                                return finalDetail;
                            });
                });
    }
}
