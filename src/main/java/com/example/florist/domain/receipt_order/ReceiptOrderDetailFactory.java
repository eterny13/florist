package com.example.florist.domain.receipt_order;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.flower.Bouquet;
import com.example.florist.domain.shared.DomainError;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceiptOrderDetailFactory {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Either<DomainError, ReceiptOrderDetail> create(
            Customer customer,
            String deliveryDateStr,
            String deliveryAddress,
            String recipientName,
            int bouquetId,
            Option<String> deliveryMessage,
            String recipientPhoneNumber,
            Seq<Bouquet> bouquets
    ) {
        Option<Bouquet> foundBouquet = bouquets.find(b -> b.getCode().value() == bouquetId);
        if (foundBouquet.isEmpty()) {
            return Either.left(new DomainError.NotFoundError("Bouquet not found for id: " + bouquetId));
        }

        Try<LocalDate> parsedDate = Try.of(() -> LocalDate.parse(deliveryDateStr, DATE_FORMATTER));
        if (parsedDate.isFailure()) {
            return Either.left(new DomainError.ValidationError("deliveryDate", "Invalid date format: " + deliveryDateStr));
        }

        return Try.of(() -> ReceiptOrderDetail.of(
                        customer,
                        parsedDate.get(),
                        deliveryAddress,
                        recipientName,
                        foundBouquet.get(),
                        deliveryMessage,
                        recipientPhoneNumber
                )).<DomainError>toEither()
                .mapLeft(t -> new DomainError.ValidationError("input", t.getMessage()));
    }
}
