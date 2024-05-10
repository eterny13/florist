package com.example.florist.api.controller.http_model.order_flower;

import com.example.florist.domain.flower.Flower;

public record OrderFlowerRequest(
        Flower flower,
        int quantity) {
}
