package com.example.florist.domain.flower

import com.example.florist.domain.shared.Quantity
import io.vavr.collection.HashMap

class FixtureBouquet {
    static Bouquet get1() {
        Bouquet.of(
                new BouquetCode(1),
                HashMap.of(
                        FixtureFlower.getRose(), new Quantity(10),
                        FixtureFlower.getCosmos(), new Quantity(5)
                )
        )
    }

    static Bouquet get2() {
        Bouquet.of(
                new BouquetCode(2),
                HashMap.of(
                        FixtureFlower.getCosmos(), new Quantity(10),
                        FixtureFlower.getTulip(), new Quantity(5)
                )
        )
    }
}
