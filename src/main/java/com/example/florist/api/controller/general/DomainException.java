package com.example.florist.api.controller.general;

import com.example.florist.domain.shared.DomainError;
import io.vavr.collection.Seq;

import java.net.URI;

public final class DomainException extends RuntimeException {
    private final Seq<DomainError> errors;
    private final URI type;

    public DomainException(Seq<DomainError> errors, URI type) {
        super(errors.map(DomainError::toString).mkString(", "));
        this.errors = errors;
        this.type = type;
    }

    public Seq<DomainError> errors() {
        return errors;
    }

    public URI type() {
        return type;
    }
}
