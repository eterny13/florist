package com.example.florist.service.flower_order;

import com.example.florist.domain.flower.Flower;
import io.vavr.collection.List;

public interface AvailableFlowerRepository {
    List<Flower> referFlowers();
}
