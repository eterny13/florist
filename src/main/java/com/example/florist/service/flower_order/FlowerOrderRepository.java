package com.example.florist.service.flower_order;

import com.example.florist.domain.flower.FlowerOrderDetail;

public interface FlowerOrderRepository {
    void persist(FlowerOrderDetail detail);
}
