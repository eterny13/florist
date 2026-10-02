package com.example.florist.api.controller.flower_order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record FlowerOrderRequest(
        @JsonProperty("flower_code")
        @NotNull Integer flowerCode,
        @JsonProperty("quantity")
        @NotNull Integer quantity) {
}
