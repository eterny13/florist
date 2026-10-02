package com.example.florist.domain.flower

import spock.lang.Specification

class FixtureFlower extends Specification {
    static Flower getRose() {
        new Flower(1, "Rose", 30, 2, 10)
    }

    static Flower getCosmos() {
        new Flower(5, "Cosmos", 20, 3, 10)
    }

    static Flower getTulip() {
        new Flower(new FlowerCode(2), "Tulip", 10, 3, 15)
    }
}
