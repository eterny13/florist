package com.example.florist.domain.receipt_order

import com.example.florist.domain.customer.FixtureCustomer
import com.example.florist.domain.flower.FixtureBouquet
import io.vavr.collection.List
import io.vavr.control.Option
import spock.lang.Specification

class ReceiptOrderDetailFactorySpec extends Specification {
    def "create ReceiptOrderDetail #label"() {
        when:
        def actual = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(),
                "2024-01-01",
                "Shinjuku, Tokyo",
                "John Aledge",
                bouquetId,
                Option.none(),
                "12345678",
                List.of(bouquet)
        )

        then:
        actual == expected

        where:
        label    | bouquetId | bouquet               | expected
        "normal" | 1         | FixtureBouquet.get1() | FixtureReceiptOrderDetail.getNormal()
    }

    def "BouquetId does not exist"() {
        when:
        ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(),
                "2024-01-01",
                "Shinjuku, Tokyo",
                "John Aledge",
                2,
                Option.none(),
                "12345678",
                List.of(FixtureBouquet.get1())
        )

        then:
        RuntimeException e = thrown()
        e.getMessage() == "NotFound Error: bouquetId does not exist"
    }
}
