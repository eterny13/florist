package com.example.florist.api.controller.customer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(includeFieldNames = false)
@EqualsAndHashCode
@Data
public class CustomerRequest {
    @JsonProperty("name")
    String name;
    @JsonProperty("email")
    String email;
}
