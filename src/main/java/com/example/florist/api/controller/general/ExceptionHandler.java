package com.example.florist.api.controller.general;

import com.example.florist.domain.shared.DomainError;
import io.vavr.collection.Seq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(DomainException exception) {
        Seq<DomainError> errors = exception.errors();
        HttpStatus status = errors.exists(DomainError.NotFoundError.class::isInstance)
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(exception.type());
        problem.setDetail(errors.map(ExceptionHandler::format).mkString(", "));
        return ResponseEntity.status(status).body(problem);
    }

    private static String format(DomainError error) {
        return switch (error) {
            case DomainError.ValidationError validation -> "Validation Error: " + validation.message();
            case DomainError.NotFoundError notFound -> "Not Found Error: " + notFound.message();
            case DomainError.OutOfStockError outOfStock -> "Out of Stock: " + outOfStock.flower().getName()
                    + " (Required: " + outOfStock.requested().value()
                    + ", Available: " + outOfStock.available().value() + ")";
            case DomainError.BusinessRuleViolation violation -> violation.message();
        };
    }
}
