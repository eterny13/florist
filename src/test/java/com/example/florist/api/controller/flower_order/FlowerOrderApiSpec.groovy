package com.example.florist.api.controller.flower_order

import com.example.florist.api.controller.flower_order.request.FlowerOrderRequest
import com.example.florist.api.controller.general.DomainException
import com.example.florist.domain.flower.Flower
import com.example.florist.domain.flower.FlowerOrderDetail
import com.example.florist.domain.shared.DomainError
import com.example.florist.domain.shared.Quantity
import com.example.florist.service.flower_order.FlowerOrderService
import io.vavr.control.Either
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@Unroll
class FlowerOrderApiSpec extends Specification {
    FlowerOrderService service = Mock()
    FlowerOrderApi api = new FlowerOrderApi(service)
    Flower flower = Flower.of(3, 'Iris', 5, 2, 7)
    FlowerOrderDetail detail = new FlowerOrderDetail(flower, new Quantity(10), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3))

    def 'post sends every request pair to service and returns created response when all succeed'() {
        given:
        def requests = [new FlowerOrderRequest(3, 10), new FlowerOrderRequest(3, 15)]

        when:
        def response = api.post(requests)

        then:
        1 * service.order(3, 10) >> Either.right(detail)
        1 * service.order(3, 15) >> Either.right(detail)
        response.statusCode == HttpStatus.CREATED
        response.body == 'Flower Order Success'
    }

    def 'post aggregates errors from each failed order and uses flower orders type'() {
        given:
        def errors = [new DomainError.NotFoundError('missing'), new DomainError.ValidationError('quantity', 'too low')]

        when:
        api.post([new FlowerOrderRequest(90, 1), new FlowerOrderRequest(3, 1)])

        then:
        1 * service.order(90, 1) >> Either.left(errors[0])
        1 * service.order(3, 1) >> Either.left(errors[1])
        def exception = thrown(DomainException)
        exception.type() == URI.create('/flower-orders')
        exception.errors().toJavaList() == errors
    }

    def 'post with empty request list succeeds without calling service'() {
        when:
        def response = api.post([])

        then:
        response.statusCode == HttpStatus.CREATED
        response.body == 'Flower Order Success'
        0 * service.order(_, _)
    }
}
