package com.example.florist.service.flower_order;

import com.example.florist.domain.flower.Bouquet;
import com.example.florist.domain.flower.BouquetCode;
import com.example.florist.domain.flower.Flower;
import com.example.florist.domain.flower.FlowerCode;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public interface AvailableFlowerRepository {
    Vector<Flower> findAllFlowers();

    Option<Flower> findFlowerByCode(FlowerCode code);

    Vector<Bouquet> findAllBouquets();

    Option<Bouquet> findBouquetByCode(BouquetCode code);
}
