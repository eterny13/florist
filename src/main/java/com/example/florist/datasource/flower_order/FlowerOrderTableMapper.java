package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static com.example.generated.db.Tables.STOCK;

@Component
@RequiredArgsConstructor
public class FlowerOrderTableMapper {
    private final DSLContext dsl;

    public void insert(FlowerOrderDetail detail) {
        dsl.insertInto(STOCK)
                .set(STOCK.FLOWER_CODE, detail.flower().getCode().value())
                .set(STOCK.FLOWER_NAME, detail.flower().getName().value())
                .set(STOCK.QUANTITY, detail.quantity().value())
                .set(STOCK.ARRIVAL_DATE, detail.arrivalDate())
                .execute();
    }
}
