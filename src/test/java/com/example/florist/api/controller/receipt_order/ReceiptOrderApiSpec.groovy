package com.example.florist.api.controller.receipt_order

import com.example.florist.api.controller.general.DomainException
import com.example.florist.api.controller.receipt_order.request.ReceiptOrderDetailRequest
import com.example.florist.domain.receipt_order.FixtureReceiptOrderDetail
import com.example.florist.domain.shared.DomainError
import com.example.florist.service.receipt_order.ReceiptOrderService
import io.vavr.control.Either
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ReceiptOrderApiSpec extends Specification {
    ReceiptOrderService service = Mock()
    ReceiptOrderApi api = new ReceiptOrderApi(service)
    def request = new ReceiptOrderDetailRequest('test_id', '2024-01-01', 'Shinjuku, Tokyo', 'John Aledge', 1, 'Happy day', '12345678')
    def detail = FixtureReceiptOrderDetail.getNormal()

    def 'post passes all request fields and returns mapped receipt response'() {
        when:
        def response = api.post(request)

        then:
        1 * service.receive('test_id', '2024-01-01', 'Shinjuku, Tokyo', 'John Aledge', 1, io.vavr.control.Option.of('Happy day'), '12345678') >> Either.right(detail)
        response.statusCode == HttpStatus.OK
        response.body.customerId == detail.customer().id.value()
        response.body.deliveryDate == '2024-01-01'
        response.body.deliveryAddress == 'Shinjuku, Tokyo'
        response.body.recipientName == 'John Aledge'
        response.body.bouquetId == 1
        response.body.deliveryMessage == null
        response.body.recipientPhoneNumber == '12345678'
    }

    def 'post converts absent optional message into Option.none'() {
        when:
        api.post(new ReceiptOrderDetailRequest('test_id', '2024-01-01', 'Shinjuku, Tokyo', 'John Aledge', 1, null, '12345678'))

        then:
        1 * service.receive('test_id', '2024-01-01', 'Shinjuku, Tokyo', 'John Aledge', 1, io.vavr.control.Option.none(), '12345678') >> Either.right(detail)
    }

    def 'post converts domain errors into exception with receipt order type'() {
        when:
        api.post(request)

        then:
        1 * service.receive(_, _, _, _, _, _, _) >> Either.left(new DomainError.NotFoundError('no customer'))
        def exception = thrown(DomainException)
        exception.type() == URI.create('/receipt-order')
        exception.errors().head() == new DomainError.NotFoundError('no customer')
    }
}
