package com.example.florist.domain.receipt_order

import com.example.florist.domain.customer.FixtureCustomer
import com.example.florist.domain.flower.FixtureBouquet
import com.example.florist.domain.shared.Quantity
import com.example.florist.domain.stock.StockAllocation
import io.vavr.collection.Vector
import io.vavr.control.Option
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@Unroll
class ReceiptOrderValuesSpec extends Specification {
    def 'delivery message normalizes null and blank options and provides defaults'() {
        expect:
        new DeliveryMessage(null) == DeliveryMessage.EMPTY
        DeliveryMessage.empty() == DeliveryMessage.EMPTY
        DeliveryMessage.ofOption(null) == DeliveryMessage.EMPTY
        !DeliveryMessage.ofOption(Option.none()).isPresent()
        !DeliveryMessage.ofOption(Option.of('   ')).isPresent()
        DeliveryMessage.ofOption(Option.of('hello')).isPresent()
        DeliveryMessage.ofOption(Option.of('hello')).getOrElse('default') == 'hello'
        DeliveryMessage.empty().getOrElse('default') == 'default'
    }

    def 'receipt detail defaults null option and allocations and withAllocations returns a copy'() {
        given:
        def customer = FixtureCustomer.get()
        def bouquet = FixtureBouquet.get1()
        def base = new ReceiptOrderDetail(customer, LocalDate.of(2024, 1, 1), new DeliveryAddress('address'), 'recipient', bouquet, null, new PhoneNumber('123'), null)
        def flower = bouquet.flowerList.head()._1
        def allocations = Vector.of(new StockAllocation(flower, LocalDate.of(2023, 12, 31), new Quantity(2)))

        when:
        def allocated = base.withAllocations(allocations)

        then:
        base.deliveryMessage() == DeliveryMessage.empty()
        base.allocations().isEmpty()
        allocated.allocations() == allocations
        allocated.deliveryMessage() == base.deliveryMessage()
        base.deliveryAddress() == new DeliveryAddress('address')
        base.recipientPhoneNumber() == new PhoneNumber('123')
        allocated.customer() == customer
        allocated.bouquet() == bouquet
        ReceiptOrderDetail.of(customer, base.deliveryDate(), 'address', 'recipient', bouquet, Option.of('note'), '123').deliveryMessage() == new DeliveryMessage(Option.of('note'))
    }

    def 'phone number and address reject null or whitespace only'() {
        when:
        new PhoneNumber(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [null, '', '  ']
    }

    def 'delivery address rejects null or whitespace only'() {
        when:
        new DeliveryAddress(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [null, '', '  ']
    }
}
