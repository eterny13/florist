package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.*;
import com.example.florist.domain.shared.Days;
import com.example.florist.domain.shared.Quantity;
import com.example.florist.service.flower_order.AvailableFlowerRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableFlowerRepositoryDb implements AvailableFlowerRepository {
    private static final Table<?> FLOWER = DSL.table(DSL.name("flower"));
    private static final Field<Integer> FLOWER_CODE = DSL.field(DSL.name("code"), Integer.class);
    private static final Field<String> FLOWER_NAME = DSL.field(DSL.name("name"), String.class);
    private static final Field<Integer> MIN_UNIT_QUANTITY = DSL.field(DSL.name("min_unit_quantity"), Integer.class);
    private static final Field<Integer> ORDER_LEAD_TIME = DSL.field(DSL.name("order_lead_time"), Integer.class);
    private static final Field<Integer> DAYS_OF_BEST_QUALITY = DSL.field(DSL.name("days_of_best_quality"), Integer.class);

    private static final Table<?> BOUQUET = DSL.table(DSL.name("bouquet"));
    private static final Field<Integer> BOUQUET_CODE = DSL.field(DSL.name("code"), Integer.class);

    private static final Table<?> BOUQUET_FLOWER = DSL.table(DSL.name("bouquet_flower"));
    private static final Field<Integer> ITEM_BOUQUET_CODE = DSL.field(DSL.name("bouquet_code"), Integer.class);
    private static final Field<Integer> ITEM_FLOWER_CODE = DSL.field(DSL.name("flower_code"), Integer.class);
    private static final Field<Integer> ITEM_QUANTITY = DSL.field(DSL.name("quantity"), Integer.class);

    private final DSLContext dsl;

    @Override
    public Vector<Flower> findAllFlowers() {
        return Vector.ofAll(dsl.select(
                        FLOWER_CODE,
                        FLOWER_NAME,
                        MIN_UNIT_QUANTITY,
                        ORDER_LEAD_TIME,
                        DAYS_OF_BEST_QUALITY
                )
                .from(FLOWER)
                .orderBy(FLOWER_CODE.asc())
                .fetch()
                .map(this::toFlower));
    }

    @Override
    public Option<Flower> findFlowerByCode(FlowerCode code) {
        return Option.of(dsl.select(
                                FLOWER_CODE,
                                FLOWER_NAME,
                                MIN_UNIT_QUANTITY,
                                ORDER_LEAD_TIME,
                                DAYS_OF_BEST_QUALITY
                        )
                        .from(FLOWER)
                        .where(FLOWER_CODE.eq(code.value()))
                        .fetchOne())
                .map(this::toFlower);
    }

    @Override
    public Vector<Bouquet> findAllBouquets() {
        java.util.HashMap<Integer, Flower> flowersByCode = new java.util.HashMap<>();
        for (Flower flower : findAllFlowers()) {
            flowersByCode.put(flower.getCode().value(), flower);
        }

        Vector<Bouquet> bouquets = Vector.empty();
        for (Record bouquetRecord : dsl.select(BOUQUET_CODE)
                .from(BOUQUET)
                .orderBy(BOUQUET_CODE.asc())
                .fetch()) {
            BouquetCode bouquetCode = new BouquetCode(bouquetRecord.get(BOUQUET_CODE));
            HashMap<Flower, Quantity> flowers = HashMap.empty();

            for (Record itemRecord : dsl.select(ITEM_FLOWER_CODE, ITEM_QUANTITY)
                    .from(BOUQUET_FLOWER)
                    .where(ITEM_BOUQUET_CODE.eq(bouquetCode.value()))
                    .orderBy(ITEM_FLOWER_CODE.asc())
                    .fetch()) {
                Flower flower = flowersByCode.get(itemRecord.get(ITEM_FLOWER_CODE));
                if (flower != null) {
                    flowers = flowers.put(flower, new Quantity(itemRecord.get(ITEM_QUANTITY)));
                }
            }
            bouquets = bouquets.append(Bouquet.ofQuantities(bouquetCode, flowers));
        }
        return bouquets;
    }

    @Override
    public Option<Bouquet> findBouquetByCode(BouquetCode code) {
        return findAllBouquets().find(bouquet -> bouquet.getCode().value() == code.value());
    }

    private Flower toFlower(Record record) {
        return new Flower(
                new FlowerCode(record.get(FLOWER_CODE)),
                new FlowerName(record.get(FLOWER_NAME)),
                new Quantity(record.get(MIN_UNIT_QUANTITY)),
                new Days(record.get(ORDER_LEAD_TIME)),
                new Days(record.get(DAYS_OF_BEST_QUALITY))
        );
    }
}
