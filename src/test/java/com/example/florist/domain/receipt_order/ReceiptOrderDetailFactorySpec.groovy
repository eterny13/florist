package com.example.florist.domain.receipt_order

import com.example.florist.domain.customer.FixtureCustomer
import com.example.florist.domain.flower.FixtureBouquet
import com.example.florist.domain.shared.DomainError
import io.vavr.collection.Vector
import io.vavr.control.Option
import spock.lang.Specification

class ReceiptOrderDetailFactorySpec extends Specification {
    def "create ReceiptOrderDetail #label"() {
        when:
        def actualResult = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(),
                "2024-01-01",
                "Shinjuku, Tokyo",
                "John Aledge",
                bouquetId,
                Option.none(),
                "12345678",
                Vector.of(bouquet)
        )

        then:
        actualResult.isRight()
        actualResult.get() == expected

        where:
        label    | bouquetId | bouquet               | expected
        "normal" | 1         | FixtureBouquet.get1() | FixtureReceiptOrderDetail.getNormal()
    }

    def "BouquetId does not exist returns NotFoundError"() {
        when:
        def actualResult = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(),
                "2024-01-01",
                "Shinjuku, Tokyo",
                "John Aledge",
                2,
                Option.none(),
                "12345678",
                Vector.of(FixtureBouquet.get1())
        )

        then:
        actualResult.isLeft()
        actualResult.getLeft() instanceof DomainError.NotFoundError
        ((DomainError.NotFoundError) actualResult.getLeft()).message() == "Bouquet not found for id: 2"
    }
}
