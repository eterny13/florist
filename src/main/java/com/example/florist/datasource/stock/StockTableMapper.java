package com.example.florist.datasource.stock;

import com.example.florist.domain.flower.Flower;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.example.generated.db.Tables.STOCK;
import static org.jooq.impl.DSL.sum;

@Component
@RequiredArgsConstructor
public class StockTableMapper {
    private final DSLContext dsl;

    private boolean isPossibleToOrder(Flower flower, int quantity, LocalDate deliveryDate) {
        var result = dsl.select(sum(STOCK.QUANTITY))
                .from(STOCK)
                .where(STOCK.FLOWER_CODE.eq(flower.getCode()))
                .and(STOCK.ARRIVAL_DATE.le(deliveryDate))
                .fetchOptional().get().value1();

        var stockQuantity = Option.of(result).isDefined() ? result.intValue() : 0;
        return quantity <= stockQuantity;
    }

    private void subtract(Flower flower, int quantity, LocalDate deliveryDate) {
        dsl.insertInto(STOCK)
                .set(STOCK.FLOWER_CODE, flower.getCode())
                .set(STOCK.FLOWER_NAME, flower.getName())
                .set(STOCK.QUANTITY, -quantity)
                .set(STOCK.ARRIVAL_DATE, deliveryDate)
                .execute();
    }

    public boolean confirm(ReceiptOrderDetail detail) {
        return detail.getBouquet().getFlowerList().map(x ->
                isPossibleToOrder(x._1, x._2, detail.getDeliveryDate())
        ).forAll(i -> i);
    }

    public void insert(ReceiptOrderDetail detail) {
        detail.getBouquet().getFlowerList().forEach(x ->
                subtract(x._1, x._2, detail.getDeliveryDate())
        );
    }
}
