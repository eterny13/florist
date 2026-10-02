package com.example.florist.api.controller.receipt_order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ReceiptOrderDetailRequest(
        @JsonProperty("customer_id")
        @NotNull String customerId,
        @JsonProperty("delivery_date")
        @NotNull String deliveryDate,
        @JsonProperty("delivery_address")
        @NotNull String deliveryAddress,
        @JsonProperty("recipient_name")
        @NotNull String recipientName,
        @JsonProperty("bouquet_id")
        @NotNull Integer bouquetId,
        @JsonProperty("delivery_message")
        String deliveryMessage,
        @JsonProperty("recipient_phone_number")
        @NotNull String recipientPhoneNumber) {
}
