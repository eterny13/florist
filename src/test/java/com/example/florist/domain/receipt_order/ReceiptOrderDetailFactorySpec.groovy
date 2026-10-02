package com.example.florist.domain.receipt_order

import com.example.florist.domain.customer.FixtureCustomer
import com.example.florist.domain.flower.FixtureBouquet
import com.example.florist.domain.shared.DomainError
import io.vavr.collection.Vector
import io.vavr.control.Option
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
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

    def 'invalid delivery date returns field validation error'() {
        when:
        def actualResult = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(), '2024/02/30', 'address', 'recipient', 1,
                Option.of('note'), '12345678', Vector.of(FixtureBouquet.get1())
        )

        then:
        actualResult.isLeft()
        actualResult.getLeft() == new DomainError.ValidationError('deliveryDate', 'Invalid date format: 2024/02/30')
    }

    def 'date format must be exact and bouquet lookup precedes date parsing'() {
        when:
        def wrongFormat = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(), '01-01-2024', 'address', 'recipient', 1,
                Option.none(), '12345678', Vector.of(FixtureBouquet.get1())
        )
        def absentBouquet = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(), 'not-a-date', 'address', 'recipient', 99,
                Option.none(), '12345678', Vector.of(FixtureBouquet.get1())
        )

        then:
        wrongFormat.getLeft() == new DomainError.ValidationError('deliveryDate', 'Invalid date format: 01-01-2024')
        absentBouquet.getLeft() == new DomainError.NotFoundError('Bouquet not found for id: 99')
    }

    def 'blank address and phone number are rejected through their value objects'() {
        when:
        def blankAddress = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(), '2024-01-01', '  ', 'recipient', 1,
                Option.none(), '12345678', Vector.of(FixtureBouquet.get1())
        )
        def blankPhone = ReceiptOrderDetailFactory.create(
                FixtureCustomer.get(), '2024-01-01', 'address', 'recipient', 1,
                Option.none(), ' ', Vector.of(FixtureBouquet.get1())
        )

        then:
        blankAddress.getLeft() == new DomainError.ValidationError('input', 'Delivery address must not be blank')
        blankPhone.getLeft() == new DomainError.ValidationError('input', 'Phone number must not be blank')
    }
}
