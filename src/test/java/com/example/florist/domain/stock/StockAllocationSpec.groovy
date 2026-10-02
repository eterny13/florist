package com.example.florist.domain.stock

import com.example.florist.domain.flower.*
import com.example.florist.domain.shared.Days
import com.example.florist.domain.shared.DomainError
import com.example.florist.domain.shared.Quantity
import io.vavr.collection.HashMap
import io.vavr.collection.Vector
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@Unroll
class StockAllocationSpec extends Specification {

    def "お届け日における日持ち日数（品質保持期限）の在庫引当判定 #label"() {
        given: "入荷日 2024-05-10、日持ち日数5日のRose（有効期限: 2024-05-15）"
        def rose = new Flower(new FlowerCode(1), new FlowerName("Rose"), new Quantity(10), new Days(2), new Days(5))
        def stock = new Stock(Vector.of(
                new StockLot(rose, new Quantity(10), new Quantity(10), LocalDate.of(2024, 5, 10))
        ))
        def bouquet = new Bouquet(new BouquetCode(1), HashMap.of(rose, new Quantity(5)))

        when: "指定のお届け日で引き当てを実行"
        def result = stock.allocate(bouquet, deliveryDate)

        then: "期待通りの成否となること"
        result.isRight() == expectedSuccess
        if (!expectedSuccess) {
            assert result.getLeft() instanceof DomainError.OutOfStockError
        }

        where:
        label                  | deliveryDate              || expectedSuccess
        "入荷日前（不可）"       | LocalDate.of(2024, 5, 9)  || false
        "入荷日当日（可能）"     | LocalDate.of(2024, 5, 10) || true
        "品質期限最終日（可能）" | LocalDate.of(2024, 5, 15) || true
        "品質期限切れ（不可）"   | LocalDate.of(2024, 5, 16) || false
    }

    def "複数ロットからのFIFO（古い入荷日順）引当と残数計算"() {
        given: "2つのロット（Lot1: 5/10入荷 残5本、Lot2: 5/12入荷 残10本）"
        def rose = new Flower(new FlowerCode(1), new FlowerName("Rose"), new Quantity(10), new Days(2), new Days(5))
        def lot1 = new StockLot(rose, new Quantity(10), new Quantity(5), LocalDate.of(2024, 5, 10))
        def lot2 = new StockLot(rose, new Quantity(10), new Quantity(10), LocalDate.of(2024, 5, 12))
        def stock = new Stock(Vector.of(lot1, lot2))
        def bouquet = new Bouquet(new BouquetCode(1), HashMap.of(rose, new Quantity(8)))

        when: "5/14 お届け日で8本引き当てる"
        def result = stock.allocate(bouquet, LocalDate.of(2024, 5, 14))

        then: "引き当てに成功し、Lot1から5本、Lot2から3本引き当てられること"
        result.isRight()
        def updatedStock = result.get()._1
        def allocations = result.get()._2

        allocations.size() == 2
        allocations.get(0).arrivalDate() == LocalDate.of(2024, 5, 10)
        allocations.get(0).quantity() == new Quantity(5)
        allocations.get(1).arrivalDate() == LocalDate.of(2024, 5, 12)
        allocations.get(1).quantity() == new Quantity(3)

        // 更新後の在庫残数
        updatedStock.lots().get(0).remainingQuantity() == new Quantity(0)
        updatedStock.lots().get(1).remainingQuantity() == new Quantity(7)
    }
}
