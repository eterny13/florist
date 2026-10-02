package com.example.florist.domain.receipt_order;

import io.vavr.control.Option;

public record DeliveryMessage(Option<String> value) {
    public static final DeliveryMessage EMPTY = new DeliveryMessage(Option.none());

    public DeliveryMessage {
        if (value == null) {
            value = Option.none();
        }
    }

    public static DeliveryMessage ofOption(Option<String> option) {
        if (option == null) {
            return EMPTY;
        }
        return new DeliveryMessage(option.filter(s -> !s.isBlank()));
    }

    public static DeliveryMessage empty() {
        return EMPTY;
    }

    public boolean isPresent() {
        return value.isDefined();
    }

    public String getOrElse(String defaultValue) {
        return value.getOrElse(defaultValue);
    }
}
