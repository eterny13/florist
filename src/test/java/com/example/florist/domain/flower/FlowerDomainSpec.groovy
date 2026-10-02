package com.example.florist.domain.flower

import com.example.florist.domain.shared.Days
import com.example.florist.domain.shared.DomainError
import com.example.florist.domain.shared.Quantity
import io.vavr.collection.HashMap
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@Unroll
class FlowerDomainSpec extends Specification {
    def 'flower code validates positivity and ordering: #value'() {
        when:
        def code = new FlowerCode(value)

        then:
        code.value() == value
        code.compareTo(new FlowerCode(other)) == expectedComparison

        where:
        value | other || expectedComparison
        1     | 2     || -1
        2     | 2     || 0
        3     | 2     || 1
    }

    def 'invalid flower code #value is rejected'() {
        when:
        new FlowerCode(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [0, -1]
    }

    def 'flower factories calculate arrival, expiration, and inclusive freshness dates'() {
        given:
        def flower = Flower.of(1, 'Rose', 10, 2, 5)
        def orderDate = LocalDate.of(2024, 5, 10)
        def arrivalDate = LocalDate.of(2024, 5, 12)

        expect:
        flower == new Flower(new FlowerCode(1), new FlowerName('Rose'), new Quantity(10), new Days(2), new Days(5))
        new Flower(1, 'Rose', 10, 2, 5) == flower
        flower.calculateArrivalDate(orderDate) == arrivalDate
        flower.calculateExpirationDate(arrivalDate) == LocalDate.of(2024, 5, 17)
        !flower.isFreshAt(arrivalDate, arrivalDate.minusDays(1))
        flower.isFreshAt(arrivalDate, arrivalDate)
        flower.isFreshAt(arrivalDate, arrivalDate.plusDays(5))
        !flower.isFreshAt(arrivalDate, arrivalDate.plusDays(6))
    }

    def 'flower order enforces minimum and computes arrival date at the threshold'() {
        given:
        def flower = Flower.of(2, 'Tulip', 10, 3, 8)

        expect:
        FlowerOrderDetail.create(flower, new Quantity(10), LocalDate.of(2024, 2, 1)).get() ==
                new FlowerOrderDetail(flower, new Quantity(10), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 4))
        FlowerOrderDetail.create(flower, new Quantity(9), LocalDate.of(2024, 2, 1)).getLeft() ==
                new DomainError.ValidationError('quantity', 'Ordered quantity 9 is lower than minimum unit quantity 10 of Tulip')
    }

    def 'bouquet code must be positive and compares numerically: #value'() {
        when:
        def code = new BouquetCode(value)

        then:
        code.compareTo(new BouquetCode(other)) == expectedComparison

        where:
        value | other || expectedComparison
        1     | 2     || -1
        2     | 2     || 0
        3     | 2     || 1
    }

    def 'non-positive bouquet codes are rejected'() {
        when:
        new BouquetCode(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [0, -1]
    }

    def 'bouquet converts supported quantity forms and exposes integer map'() {
        given:
        def rose = Flower.of(1, 'Rose', 1, 0, 1)
        def tulip = Flower.of(2, 'Tulip', 1, 0, 1)

        when:
        def bouquet = Bouquet.of(new BouquetCode(1), HashMap.of(rose, 4, tulip, new Quantity(6)))

        then:
        bouquet.flowerList.get(rose).get() == new Quantity(4)
        bouquet.flowerList.get(tulip).get() == new Quantity(6)
        bouquet.getFlowerQuantityMap().get(rose).get() == 4
        bouquet.getFlowerQuantityMap().get(tulip).get() == 6
        Bouquet.ofQuantities(new BouquetCode(2), bouquet.flowerList).flowerList == bouquet.flowerList
    }

    def 'bouquet rejects unsupported quantity type'() {
        when:
        Bouquet.of(new BouquetCode(1), HashMap.of(Flower.of(1, 'Rose', 1, 0, 1), 'four'))

        then:
        def error = thrown(IllegalArgumentException)
        error.message == 'Bouquet flower quantity must be a Quantity or Integer'
    }

    def 'flower name rejects null and whitespace only'() {
        when:
        new FlowerName(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [null, '', '  ']
    }
}
