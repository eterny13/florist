package com.example.florist.domain.flower

class FixtureFlower {
    static Flower getRose() {
        of(1, "Rose", 30, 2, 10)
    }

    static Flower getCosmos() {
        of(5, "Cosmos", 20, 3, 10)
    }

    static Flower getTulip() {
        of(2, "Tulip", 10, 3, 15)
    }

    static Flower of(int code, String name, int minUnitQuantity, int orderLeadTime, int daysOfBestQuality) {
        Flower.of(code, name, minUnitQuantity, orderLeadTime, daysOfBestQuality)
    }
}
