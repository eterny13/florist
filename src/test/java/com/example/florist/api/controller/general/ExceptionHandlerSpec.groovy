package com.example.florist.api.controller.general

import com.example.florist.domain.flower.Flower
import com.example.florist.domain.shared.DomainError
import com.example.florist.domain.shared.Quantity
import io.vavr.collection.Vector
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ExceptionHandlerSpec extends Specification {
    def handler = new ExceptionHandler()

    def 'formats each domain error and selects bad request unless any not found error is present'() {
        given:
        def errors = Vector.of(
                new DomainError.ValidationError('email', 'invalid'),
                new DomainError.OutOfStockError(Flower.of(1, 'Rose', 1, 0, 1), new Quantity(5), new Quantity(2)),
                new DomainError.BusinessRuleViolation('rule failed')
        )

        when:
        def response = handler.handleDomainException(new DomainException(errors, URI.create('/test')))

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
        response.body.status == 400
        response.body.type == URI.create('/test')
        response.body.detail == 'Validation Error: invalid, Out of Stock: Rose (Required: 5, Available: 2), rule failed'
    }

    def 'not found anywhere in a batch selects not found and formats message'() {
        when:
        def response = handler.handleDomainException(new DomainException(Vector.of(
                new DomainError.ValidationError('name', 'bad'),
                new DomainError.NotFoundError('missing bouquet')
        ), URI.create('/receipt-order')))

        then:
        response.statusCode == HttpStatus.NOT_FOUND
        response.body.status == 404
        response.body.detail == 'Validation Error: bad, Not Found Error: missing bouquet'
    }
}
