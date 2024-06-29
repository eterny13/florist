package com.example.florist.domain.receipt_order

import com.example.florist.domain.customer.FixtureCustomer
import com.example.florist.domain.flower.FixtureBouquet
import io.vavr.control.Option

import java.time.LocalDate

class FixtureReceiptOrderDetail {
    static getNormal() {
        new ReceiptOrderDetail(
                FixtureCustomer.get(),
                LocalDate.of(2024, 1, 1),
                "Shinjuku, Tokyo",
                "John Aledge",
                FixtureBouquet.get1(),
                Option.<String> none(),
                "12345678"
        )
    }
}
