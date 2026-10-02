package com.example.florist.service.flower_order

import com.example.florist.domain.flower.FixtureFlower
import com.example.florist.domain.flower.Flower
import com.example.florist.domain.flower.FlowerCode
import com.example.florist.domain.flower.FlowerOrderDetail
import com.example.florist.domain.shared.DomainError
import com.example.florist.domain.shared.Quantity
import com.example.florist.service.stock.StockRepository
import io.vavr.control.Option
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@Unroll
class FlowerOrderServiceSpec extends Specification {
    FlowerOrderRepository flowerOrderRepository = Mock()
    AvailableFlowerRepository availableFlowerRepository = Mock()
    StockRepository stockRepository = Mock()
    FlowerOrderService service = new FlowerOrderService(flowerOrderRepository, availableFlowerRepository, stockRepository)
    Flower flower = FixtureFlower.of(7, 'Rose', 10, 3, 12)

    def 'order with explicit date persists order and arrival exactly once'() {
        given:
        def expected = new FlowerOrderDetail(flower, new Quantity(15), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 4))

        when:
        def result = service.order(7, 15, LocalDate.of(2024, 4, 1))

        then:
        1 * availableFlowerRepository.findFlowerByCode(new FlowerCode(7)) >> Option.of(flower)
        result.isRight()
        result.get() == expected
        1 * flowerOrderRepository.persist(expected)
        1 * stockRepository.insertArrival(expected)
    }

    def 'unknown flower returns NotFoundError and has no writes'() {
        given:
        when:
        def result = service.order(99, 10, LocalDate.of(2024, 4, 1))

        then:
        result.isLeft()
        result.getLeft() == new DomainError.NotFoundError('Undefined flower code: 99')
        1 * availableFlowerRepository.findFlowerByCode(new FlowerCode(99)) >> Option.none()
        0 * flowerOrderRepository.persist(_)
        0 * stockRepository.insertArrival(_)
    }

    def 'quantity below minimum returns validation error and has no writes'() {
        given:
        availableFlowerRepository.findFlowerByCode(new FlowerCode(7)) >> Option.of(flower)

        when:
        def result = service.order(7, 9, LocalDate.of(2024, 4, 1))

        then:
        result.isLeft()
        result.getLeft() == new DomainError.ValidationError('quantity', 'Ordered quantity 9 is lower than minimum unit quantity 10 of Rose')
        0 * flowerOrderRepository.persist(_)
        0 * stockRepository.insertArrival(_)
    }
}
