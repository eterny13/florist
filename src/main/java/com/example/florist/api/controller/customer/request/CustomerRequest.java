package com.example.florist.api.controller.customer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotNull @JsonProperty("name") String name,
        @NotNull @JsonProperty("email") String email
) {
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
