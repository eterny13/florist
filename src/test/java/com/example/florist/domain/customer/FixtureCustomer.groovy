package com.example.florist.domain.customer

import spock.lang.Specification

class FixtureCustomer extends Specification {
    static Customer get() {
        new Customer(new CustomerId("test_id"), new CustomerName("John Doe"), new EmailAddress("john@example.com"))
    }
}
