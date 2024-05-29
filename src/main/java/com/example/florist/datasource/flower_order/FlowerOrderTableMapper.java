package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.example.generated.db.Tables.STOCK;

@Component
@RequiredArgsConstructor
public class FlowerOrderTableMapper {
    private final DSLContext dsl;

    public void insert(FlowerOrderDetail detail) {
        dsl.insertInto(STOCK)
                .set(STOCK.FLOWER_CODE, detail.getFlower().getCode())
                .set(STOCK.FLOWER_NAME, detail.getFlower().getName())
                .set(STOCK.QUANTITY, detail.getQuantity())
                .set(STOCK.ARRIVAL_DATE, detail.getArrivalDate())
                .execute();
    }

    public void insert(ReceiptOrderDetail detail, LocalDate deliveryDate) {
        detail.getBouquet().getFlowerList().map(x ->
                dsl.insertInto(STOCK)
                        .set(STOCK.FLOWER_CODE, x._1.getCode())
                        .set(STOCK.FLOWER_NAME, x._1.getName())
                        .set(STOCK.QUANTITY, x._2)
                        .set(STOCK.ARRIVAL_DATE, deliveryDate)
                        .execute()
        );
    }
}
