package com.example.florist.api.controller.customer;

import com.example.florist.api.controller.customer.request.CustomerRequest;
import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.service.customer.CustomerService;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerApi {
    private static final URI CUSTOMERS_URI = URI.create("/customers");
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody CustomerRequest request) {
        String name = request.name() != null ? request.name() : "";
        String email = request.email() != null ? request.email() : "";

        Either<Seq<DomainError>, Customer> result = service.register(name, email);

        if (result.isLeft()) {
            Seq<DomainError> errors = result.getLeft();
            String errorMessage = errors.map(e -> {
                if (e instanceof DomainError.ValidationError ve) {
                    return "Validation Error: " + ve.message();
                }
                return e.toString();
            }).mkString(", ");

            var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setType(CUSTOMERS_URI);
            problemDetail.setDetail(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }

        Customer customer = result.get();
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer ID: " + customer.getId().value());
    }
}
