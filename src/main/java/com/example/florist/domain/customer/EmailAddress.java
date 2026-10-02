package com.example.florist.domain.customer;

import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+$");

    public EmailAddress {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }
}
