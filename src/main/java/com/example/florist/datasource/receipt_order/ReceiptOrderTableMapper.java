package com.example.florist.datasource.receipt_order;

import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static com.example.generated.db.Tables.ORDER_DETAIL;

@Component
@RequiredArgsConstructor
public class ReceiptOrderTableMapper {
    private final DSLContext dsl;

    public void insert(ReceiptOrderDetail receiptOrderDetail) {
        dsl.insertInto(ORDER_DETAIL)
                .set(ORDER_DETAIL.CUSTOMER_ID, receiptOrderDetail.customer().getId().value())
                .set(ORDER_DETAIL.BOUQUET_CODE, receiptOrderDetail.bouquet().getCode().value())
                .set(ORDER_DETAIL.RECIPIENT_NAME, receiptOrderDetail.recipientName())
                .set(ORDER_DETAIL.DELIVERY_ADDRESS, receiptOrderDetail.deliveryAddress().value())
                .set(ORDER_DETAIL.DELIVERY_DATE, receiptOrderDetail.deliveryDate())
                .set(ORDER_DETAIL.DELIVERY_MESSAGE, receiptOrderDetail.deliveryMessage().value().getOrNull())
                .set(ORDER_DETAIL.RECIPIENT_PHONE_NUMBER, receiptOrderDetail.recipientPhoneNumber().value())
                .execute();
    }
}
