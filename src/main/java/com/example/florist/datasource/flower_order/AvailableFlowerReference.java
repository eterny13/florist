package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.Bouquet;
import com.example.florist.domain.flower.Flower;
import com.example.florist.service.flower_order.AvailableFlowerRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import org.springframework.stereotype.Component;

@Component
public class AvailableFlowerReference implements AvailableFlowerRepository {
    @Override
    public List<Flower> referFlowers() {
        return List.of(
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
    }

    @Override
    public List<Bouquet> referBouquets() {
        var availableFlowers = referFlowers();
        return List.of(
                new Bouquet(
                        1,
                        HashMap.of(
                                availableFlowers.get(0), 4,
                                availableFlowers.get(1), 3,
                                availableFlowers.get(2), 3,
                                availableFlowers.get(3), 3,
                                availableFlowers.get(4), 4
                        )
                ),
                new Bouquet(
                        2,
                        HashMap.of(
                                availableFlowers.get(0), 4,
                                availableFlowers.get(3), 3,
                                availableFlowers.get(4), 3,
                                availableFlowers.get(5), 3,
                                availableFlowers.get(6), 4
                        )
                ),
                new Bouquet(
                        3,
                        HashMap.of(
                                availableFlowers.get(2), 4,
                                availableFlowers.get(3), 4,
                                availableFlowers.get(4), 3,
                                availableFlowers.get(5), 3,
                                availableFlowers.get(6), 3
                        )
                ),
                new Bouquet(
                        4,
                        HashMap.of(
                                availableFlowers.get(2), 4,
                                availableFlowers.get(3), 4,
                                availableFlowers.get(7), 3,
                                availableFlowers.get(8), 3,
                                availableFlowers.get(9), 3
                        )
                ),
                new Bouquet(
                        5,
                        HashMap.of(
                                availableFlowers.get(0), 2,
                                availableFlowers.get(1), 2,
                                availableFlowers.get(2), 4,
                                availableFlowers.get(3), 4,
                                availableFlowers.get(7), 3,
                                availableFlowers.get(8), 2,
                                availableFlowers.get(9), 2
                        )
                )
        );
    }

}
