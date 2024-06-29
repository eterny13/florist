package com.example.florist.domain.flower

import io.vavr.collection.HashMap
import spock.lang.Specification

class FixtureBouquet extends Specification {
    static get1() {
        new Bouquet(
                1,
                HashMap.<Flower, Integer> of(
                        FixtureFlower.getRose(), 10,
                        FixtureFlower.getCosmos(), 5
                )
        )
    }

    static get2() {
        new Bouquet(
                2,
                HashMap.<Flower, Integer> of(
                        FixtureFlower.getCosmos(), 10,
                        FixtureFlower.getTulip(), 5
                )
        )
    }
}
