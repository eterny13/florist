package com.example.florist.service.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowerOrderService {
    private final FlowerOrderRepository flowerOrderRepository;
    private final AvailableFlowerRepository availableFlowerRepository;

    public void order(int flowerCode, int quantity) {
        var flowers = availableFlowerRepository.referFlowers();
        var flower = flowers.find(x -> x.getCode() == flowerCode);
        var detail = new FlowerOrderDetail(flower, quantity);
        flowerOrderRepository.persist(detail);
    }
}
