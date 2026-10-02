package com.example.florist.service.flower_order;

import com.example.florist.domain.flower.FlowerCode;
import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.domain.shared.Quantity;
import com.example.florist.service.stock.StockRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FlowerOrderService {
    private final FlowerOrderRepository flowerOrderRepository;
    private final AvailableFlowerRepository availableFlowerRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Either<DomainError, FlowerOrderDetail> order(int flowerCode, int quantity) {
        return order(flowerCode, quantity, LocalDate.now());
    }

    @Transactional
    public Either<DomainError, FlowerOrderDetail> order(int flowerCode, int quantity, LocalDate orderDate) {
        return availableFlowerRepository.findFlowerByCode(new FlowerCode(flowerCode))
                .toEither(() -> (DomainError) new DomainError.NotFoundError("Undefined flower code: " + flowerCode))
                .flatMap(flower -> FlowerOrderDetail.create(flower, new Quantity(quantity), orderDate))
                .map(detail -> {
                    flowerOrderRepository.persist(detail);
                    stockRepository.insertArrival(detail);
                    return detail;
                });
    }
}
