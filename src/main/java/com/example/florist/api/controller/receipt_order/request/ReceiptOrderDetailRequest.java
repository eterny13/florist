package com.example.florist.api.controller.receipt_order.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReceiptOrderDetailRequest(
        @JsonProperty("customer_id")
        String customerId,
        @JsonProperty("delivery_date")
        String deliveryDate,
        @JsonProperty("delivery_address")
        String deliveryAddress,
        @JsonProperty("recipient_name")
        String recipientName,
        @JsonProperty("bouquet_id")
        int bouquetId,
        @JsonProperty("delivery_message")
        String deliveryMessage,
        @JsonProperty("recipient_phone_number")
        String recipientPhoneNumber) {
}
