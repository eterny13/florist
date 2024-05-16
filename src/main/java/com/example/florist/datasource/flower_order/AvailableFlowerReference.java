package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.Flower;
import com.example.florist.service.flower_order.AvailableFlowerRepository;
import io.vavr.collection.List;
import org.springframework.stereotype.Component;

@Component
public class AvailableFlowerReference implements AvailableFlowerRepository {
    @Override
    public List<Flower> referFlowers() {
        var availableFlowers = List.of(
                new Flower(1, "Rose", 30, 2, 10),
                new Flower(2, "Anabel", 10, 3, 8),
                new Flower(3, "Kasumi", 10, 5, 12),
                new Flower(4, "Fragrant Olive", 10, 5, 14),
                new Flower(5, "Cosmos", 20, 3, 10),
                new Flower(6, "Sumire", 10, 3, 10),
                new Flower(7, "Cyclamen", 20, 3, 10),
                new Flower(8, "BellFlower", 20, 5, 7),
                new Flower(9, "Peony", 20, 5, 7),
                new Flower(10, "Tulip", 30, 5, 7)
        );
        return availableFlowers;
    }
}
