package com.example.florist.datasource.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.service.flower_order.FlowerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlowerOrderRepositoryDb implements FlowerOrderRepository {
    private final FlowerOrderTableMapper flowerOrderTableMapper;

    @Override
    public void persist(FlowerOrderDetail detail) {
        flowerOrderTableMapper.insert(detail);
    }
}
