package com.example.florist.domain.flower;

import io.vavr.collection.HashMap;
import lombok.Value;


@Value
public class Bouquet {
    int code;
    HashMap<Flower, Integer> flowerList;
}
