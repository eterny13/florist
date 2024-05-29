package com.example.florist.domain.receipt_order;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.flower.Bouquet;
import io.vavr.control.Option;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ReceiptOrderDetail {
    Customer customer;
    LocalDate deliveryDate;
    String deliveryAddress;
    String recipientName;
    Bouquet bouquet;
    Option<String> deliveryMessage;
    String recipientPhoneNumber;
}
