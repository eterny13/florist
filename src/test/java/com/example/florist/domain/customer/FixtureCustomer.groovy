package com.example.florist.domain.customer

class FixtureCustomer {
    static Customer get() {
        of("test_id", "John Doe", "john@example.com")
    }

    static Customer of(String id, String name, String email) {
        new Customer(new CustomerId(id), new CustomerName(name), new EmailAddress(email))
    }
}
