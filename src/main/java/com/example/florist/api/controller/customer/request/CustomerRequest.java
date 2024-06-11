package com.example.florist.api.controller.customer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CustomerRequest {
    @JsonProperty("name")
    String name;
    @JsonProperty("email")
    String email;
}
