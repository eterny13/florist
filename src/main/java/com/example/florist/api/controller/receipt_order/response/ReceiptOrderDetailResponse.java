package com.example.florist.api.controller.receipt_order.response;

import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.Value;

@ToString(includeFieldNames = false)
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptOrderDetailResponse {
    @JsonProperty("customer_id")
    String customerId;
    @JsonProperty("delivery_date")
    String deliveryDate;
    @JsonProperty("delivery_address")
    String deliveryAddress;
    @JsonProperty("recipient_name")
    String recipientName;
    @JsonProperty("bouquet_id")
    int bouquetId;
    @JsonProperty("delivery_message")
    String deliveryMessage;
    @JsonProperty("recipient_phone_number")
    String recipientPhoneNumber;

    public static ReceiptOrderDetailResponse of(ReceiptOrderDetail receiptOrderDetail) {
        return new ReceiptOrderDetailResponse(
                receiptOrderDetail.getCustomer().getId(),
                receiptOrderDetail.getDeliveryDate().toString(),
                receiptOrderDetail.getDeliveryAddress(),
                receiptOrderDetail.getRecipientName(),
                receiptOrderDetail.getBouquet().getCode(),
                receiptOrderDetail.getDeliveryMessage().getOrElse(""),
                receiptOrderDetail.getRecipientPhoneNumber()
        );
    }
}
