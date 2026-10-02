package com.example.florist.api.controller.customer.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerRequest(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email
) {
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
