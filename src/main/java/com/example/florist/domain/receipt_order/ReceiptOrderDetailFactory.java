package com.example.florist.domain.receipt_order;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.flower.Bouquet;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ReceiptOrderDetailFactory {
    public static ReceiptOrderDetail create(
            Customer customer,
            String deliveryDateStr,
            String deliveryAddress,
            String recipientName,
            int bouquetId,
            Option<String> deliveryMessage,
            String recipientPhoneNumber,
            List<Bouquet> bouquets) {
        var bouquet = bouquets.find(x -> x.getCode() == bouquetId);
        return new ReceiptOrderDetail(
                customer,
                LocalDate.parse(deliveryDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                deliveryAddress,
                recipientName,
                bouquet.getOrElseThrow(() -> new RuntimeException("NotFound Error: bouquetId does not exist")),
                deliveryMessage,
                recipientPhoneNumber);
    }
}
