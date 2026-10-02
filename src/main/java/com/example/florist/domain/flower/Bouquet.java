package com.example.florist.domain.flower;

import com.example.florist.domain.shared.Quantity;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.Value;

@Value
public class Bouquet {
    BouquetCode code;
    Map<Flower, Quantity> flowerList;

    public static Bouquet of(BouquetCode code, Map<Flower, ?> flowers) {
        HashMap<Flower, Quantity> converted = HashMap.empty();
        for (Tuple2<Flower, ?> flower : flowers) {
            converted = converted.put(flower._1, toQuantity(flower._2));
        }
        return new Bouquet(code, converted);
    }

    private static Quantity toQuantity(Object value) {
        if (value instanceof Quantity quantity) {
            return quantity;
        }
        if (value instanceof Integer quantity) {
            return new Quantity(quantity);
        }
        throw new IllegalArgumentException("Bouquet flower quantity must be a Quantity or Integer");
    }

    public static Bouquet ofQuantities(BouquetCode code, Map<Flower, Quantity> flowers) {
        return new Bouquet(code, flowers);
    }

    public HashMap<Flower, Integer> getFlowerQuantityMap() {
        HashMap<Flower, Integer> quantities = HashMap.empty();
        for (Tuple2<Flower, Quantity> flower : flowerList) {
            quantities = quantities.put(flower._1, flower._2.value());
        }
        return quantities;
    }
}
