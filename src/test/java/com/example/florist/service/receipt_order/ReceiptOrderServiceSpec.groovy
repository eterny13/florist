package com.example.florist.service.receipt_order

import com.example.florist.domain.customer.CustomerId
import com.example.florist.domain.customer.Customer
import com.example.florist.domain.flower.Bouquet
import com.example.florist.domain.flower.BouquetCode
import com.example.florist.domain.flower.Flower
import com.example.florist.domain.receipt_order.ReceiptOrderDetail
import com.example.florist.domain.shared.DomainError
import com.example.florist.domain.shared.Quantity
import com.example.florist.domain.stock.Stock
import com.example.florist.domain.stock.StockLot
import com.example.florist.service.customer.CustomerRepository
import com.example.florist.service.flower_order.AvailableFlowerRepository
import com.example.florist.service.stock.StockRepository
import io.vavr.collection.HashMap
import io.vavr.collection.Vector
import io.vavr.control.Option
import spock.lang.Specification

import java.time.LocalDate

class ReceiptOrderServiceSpec extends Specification {
    AvailableFlowerRepository availableFlowerRepository = Mock()
    CustomerRepository customerRepository = Mock()
    ReceiptOrderRepository receiptOrderRepository = Mock()
    StockRepository stockRepository = Mock()

    ReceiptOrderService service = new ReceiptOrderService(
            availableFlowerRepository,
            customerRepository,
            receiptOrderRepository,
            stockRepository
    )

    def rose = Flower.of(1, "Rose", 30, 2, 10)
    def cosmos = Flower.of(5, "Cosmos", 20, 3, 10)
    def customer = Customer.of("cust01", "Taro", "taro@example.com")
    def bouquet = Bouquet.of(new BouquetCode(1), HashMap.of(rose, new Quantity(5), cosmos, new Quantity(3)))

    def "正常系：在庫が十分にある場合、注文が受理されリポジトリに保存される"() {
        given:
        customerRepository.findById(new CustomerId("cust01")) >> Option.of(customer)
        availableFlowerRepository.findAllBouquets() >> Vector.of(bouquet)
        stockRepository.findAllStock() >> new Stock(Vector.of(
                StockLot.of(rose, 20, 20, LocalDate.of(2024, 6, 1)),
                StockLot.of(cosmos, 20, 20, LocalDate.of(2024, 6, 1))
        ))

        when:
        def result = service.receive(
                "cust01",
                "2024-06-05",
                "Tokyo, Japan",
                "Hanako",
                1,
                Option.of("Happy Birthday"),
                "090-1234-5678"
        )

        then:
        result.isRight()
        result.get().customer() == customer
        result.get().bouquet() == bouquet
        result.get().deliveryDate() == LocalDate.of(2024, 6, 5)

        1 * receiptOrderRepository.persist(_ as ReceiptOrderDetail)
        1 * stockRepository.saveAllocations(_ as ReceiptOrderDetail)
    }

    def "異常系：顧客が存在しない場合は NotFoundError が返る"() {
        given:
        customerRepository.findById(new CustomerId("not_exist")) >> Option.none()

        when:
        def result = service.receive(
                "not_exist",
                "2024-06-05",
                "Tokyo, Japan",
                "Hanako",
                1,
                Option.none(),
                "090-1234-5678"
        )

        then:
        result.isLeft()
        result.getLeft() instanceof DomainError.NotFoundError

        0 * receiptOrderRepository.persist(_)
        0 * stockRepository.saveAllocations(_)
    }

    def "異常系：在庫不足（日持ち期限切れ）の場合は OutOfStockError が返る"() {
        given: "入荷日が 2024-05-01（日持ち10日 -> 有効期限 2024-05-11）"
        customerRepository.findById(new CustomerId("cust01")) >> Option.of(customer)
        availableFlowerRepository.findAllBouquets() >> Vector.of(bouquet)
        stockRepository.findAllStock() >> new Stock(Vector.of(
                StockLot.of(rose, 20, 20, LocalDate.of(2024, 5, 1)),
                StockLot.of(cosmos, 20, 20, LocalDate.of(2024, 5, 1))
        ))

        when: "お届け日が 2024-06-05（期限切れ）"
        def result = service.receive(
                "cust01",
                "2024-06-05",
                "Tokyo, Japan",
                "Hanako",
                1,
                Option.none(),
                "090-1234-5678"
        )

        then:
        result.isLeft()
        result.getLeft() instanceof DomainError.OutOfStockError

        0 * receiptOrderRepository.persist(_)
        0 * stockRepository.saveAllocations(_)
    }
}
