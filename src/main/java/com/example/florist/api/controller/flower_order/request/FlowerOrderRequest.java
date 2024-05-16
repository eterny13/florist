package com.example.florist.api.controller.flower_order.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlowerOrderRequest(
        @JsonProperty("flower_code")
        int flowerCode,
        @JsonProperty("quantity")
        int quantity) {
}
