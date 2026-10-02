package com.example.florist.domain.receipt_order;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.flower.Bouquet;
import com.example.florist.domain.stock.StockAllocation;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.time.LocalDate;

public record ReceiptOrderDetail(
        Customer customer,
        LocalDate deliveryDate,
        String deliveryAddress,
        String recipientName,
        Bouquet bouquet,
        Option<String> deliveryMessage,
        String recipientPhoneNumber,
        Vector<StockAllocation> allocations
) {
    public ReceiptOrderDetail {
        if (deliveryMessage == null) {
            deliveryMessage = Option.none();
        }
        if (allocations == null) {
            allocations = Vector.empty();
        }
    }

    public static ReceiptOrderDetail of(
            Customer customer,
            LocalDate deliveryDate,
            String deliveryAddress,
            String recipientName,
            Bouquet bouquet,
            Option<String> deliveryMessage,
            String recipientPhoneNumber
    ) {
        return new ReceiptOrderDetail(
                customer,
                deliveryDate,
                deliveryAddress,
                recipientName,
                bouquet,
                deliveryMessage,
                recipientPhoneNumber,
                Vector.empty()
        );
    }

    public ReceiptOrderDetail withAllocations(Vector<StockAllocation> allocations) {
        return new ReceiptOrderDetail(
                customer,
                deliveryDate,
                deliveryAddress,
                recipientName,
                bouquet,
                deliveryMessage,
                recipientPhoneNumber,
                allocations
        );
    }
}
