package com.example.florist.datasource.stock;

import com.example.florist.domain.flower.Flower;
import com.example.florist.domain.flower.FlowerCode;
import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.domain.shared.Quantity;
import com.example.florist.domain.stock.Stock;
import com.example.florist.domain.stock.StockAllocation;
import com.example.florist.domain.stock.StockLot;
import com.example.florist.service.flower_order.AvailableFlowerRepository;
import io.vavr.collection.Vector;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.generated.db.Tables.STOCK;

@Component
@RequiredArgsConstructor
public class StockTableMapper {
    private final DSLContext dsl;
    private final AvailableFlowerRepository availableFlowerRepository;

    public Stock findAllStock() {
        var records = dsl.select(STOCK.FLOWER_CODE, STOCK.QUANTITY, STOCK.ARRIVAL_DATE)
                .from(STOCK)
                .fetch();

        Map<FlowerCode, Flower> flowerMap = availableFlowerRepository.findAllFlowers()
                .toJavaStream()
                .collect(Collectors.toMap(Flower::getCode, f -> f));

        Map<String, Integer> netQuantityMap = records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.get(STOCK.FLOWER_CODE) + "_" + r.get(STOCK.ARRIVAL_DATE),
                        Collectors.summingInt(r -> r.get(STOCK.QUANTITY))
                ));

        Map<String, Integer> initialQuantityMap = records.stream()
                .filter(r -> r.get(STOCK.QUANTITY) > 0)
                .collect(Collectors.groupingBy(
                        r -> r.get(STOCK.FLOWER_CODE) + "_" + r.get(STOCK.ARRIVAL_DATE),
                        Collectors.summingInt(r -> r.get(STOCK.QUANTITY))
                ));

        Vector<StockLot> lots = Vector.empty();
        for (var entry : initialQuantityMap.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("_");
            int flowerCode = Integer.parseInt(parts[0]);
            LocalDate arrivalDate = LocalDate.parse(parts[1]);

            Flower flower = flowerMap.get(new FlowerCode(flowerCode));
            if (flower != null) {
                int initial = entry.getValue();
                int remaining = netQuantityMap.getOrDefault(key, 0);
                if (remaining > 0) {
                    lots = lots.append(new StockLot(flower, new Quantity(initial), new Quantity(remaining), arrivalDate));
                }
            }
        }

        return new Stock(lots.sorted(Comparator.comparing(StockLot::arrivalDate)));
    }

    public void insertArrival(FlowerOrderDetail detail) {
        dsl.insertInto(STOCK)
                .set(STOCK.FLOWER_CODE, detail.flower().getCode().value())
                .set(STOCK.FLOWER_NAME, detail.flower().getName().value())
                .set(STOCK.QUANTITY, detail.quantity().value())
                .set(STOCK.ARRIVAL_DATE, detail.arrivalDate())
                .execute();
    }

    public void saveAllocations(ReceiptOrderDetail detail) {
        for (StockAllocation allocation : detail.allocations()) {
            dsl.insertInto(STOCK)
                    .set(STOCK.FLOWER_CODE, allocation.flower().getCode().value())
                    .set(STOCK.FLOWER_NAME, allocation.flower().getName().value())
                    .set(STOCK.QUANTITY, -allocation.quantity().value())
                    .set(STOCK.ARRIVAL_DATE, allocation.arrivalDate())
                    .execute();
        }
    }
}
