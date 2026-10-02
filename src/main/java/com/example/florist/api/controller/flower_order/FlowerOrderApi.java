package com.example.florist.api.controller.flower_order;

import com.example.florist.api.controller.flower_order.request.FlowerOrderRequest;
import com.example.florist.api.controller.general.DomainException;
import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.service.flower_order.FlowerOrderService;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/flower-orders")
@RequiredArgsConstructor
public class FlowerOrderApi {
    private static final URI FLOWER_ORDERS_URI = URI.create("/flower-orders");
    private final FlowerOrderService flowerOrderService;

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody List<@NotNull @Valid FlowerOrderRequest> requests) {
        Vector<Either<DomainError, FlowerOrderDetail>> results = Vector.ofAll(requests)
                .map(request -> flowerOrderService.order(request.flowerCode(), request.quantity()));

        Vector<DomainError> errors = results
                .filter(Either::isLeft)
                .map(Either::getLeft);
        if (errors.nonEmpty()) {
            throw new DomainException(errors, FLOWER_ORDERS_URI);
        }

        return results.foldLeft(
                ResponseEntity.status(HttpStatus.CREATED).body("Flower Order Success"),
                (response, result) -> result.fold(
                        error -> {
                            throw new DomainException(Vector.of(error), FLOWER_ORDERS_URI);
                        },
                        detail -> response
                )
        );
    }
}
