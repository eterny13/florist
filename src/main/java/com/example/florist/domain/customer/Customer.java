package com.example.florist.domain.customer;

import com.example.florist.domain.shared.DomainError;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.Value;

@Value
public class Customer {
    CustomerId id;
    CustomerName name;
    EmailAddress email;

    public static Validation<Seq<DomainError>, Customer> create(String nameStr, String emailStr) {
        return Validation.combine(
                validateName(nameStr),
                validateEmail(emailStr)
        ).ap((name, email) -> new Customer(CustomerId.generate(), name, email));
    }

    public static Customer reconstruct(CustomerId id, CustomerName name, EmailAddress email) {
        return new Customer(id, name, email);
    }

    public static Customer of(String id, String name, String email) {
        return new Customer(new CustomerId(id), new CustomerName(name), new EmailAddress(email));
    }

    private static Validation<DomainError, CustomerName> validateName(String name) {
        return Option.of(name)
                .filter(n -> !n.isBlank())
                .map(CustomerName::new)
                .toValidation(new DomainError.ValidationError("name", "Validation Error: Invalid Name"));
    }

    private static Validation<DomainError, EmailAddress> validateEmail(String email) {
        return Option.of(email)
                .filter(e -> e != null && e.contains("@"))
                .map(EmailAddress::new)
                .toValidation(new DomainError.ValidationError("email", "Validation Error: Invalid Email"));
    }
}
