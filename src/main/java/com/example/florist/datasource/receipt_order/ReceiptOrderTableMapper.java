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
                .set(ORDER_DETAIL.CUSTOMER_ID, receiptOrderDetail.getCustomer().getId())
                .set(ORDER_DETAIL.BOUQUET_CODE, receiptOrderDetail.getBouquet().getCode())
                .set(ORDER_DETAIL.RECIPIENT_NAME, receiptOrderDetail.getRecipientName())
                .set(ORDER_DETAIL.DELIVERY_ADDRESS, receiptOrderDetail.getDeliveryAddress())
                .set(ORDER_DETAIL.DELIVERY_DATE, receiptOrderDetail.getDeliveryDate())
                .set(ORDER_DETAIL.DELIVERY_MESSAGE, receiptOrderDetail.getDeliveryMessage().get())
                .set(ORDER_DETAIL.RECIPIENT_PHONE_NUMBER, receiptOrderDetail.getRecipientPhoneNumber())
                .execute();
    }
}
