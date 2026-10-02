package com.example.florist.api.controller.receipt_order;

import com.example.florist.api.controller.receipt_order.request.ReceiptOrderDetailRequest;
import com.example.florist.api.controller.receipt_order.response.ReceiptOrderDetailResponse;
import com.example.florist.api.controller.general.DomainException;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.service.receipt_order.ReceiptOrderService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.vavr.collection.Vector;

import java.net.URI;

@RestController
@RequestMapping("/receipt-order")
@RequiredArgsConstructor
public class ReceiptOrderApi {
    private static final URI RECEIPT_ORDER_URI = URI.create("/receipt-order");
    private final ReceiptOrderService receiptOrderService;

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody ReceiptOrderDetailRequest request) {
        Either<DomainError, ReceiptOrderDetail> result = receiptOrderService.receive(
                request.customerId(),
                request.deliveryDate(),
                request.deliveryAddress(),
                request.recipientName(),
                request.bouquetId(),
                Option.of(request.deliveryMessage()),
                request.recipientPhoneNumber()
        );

        return result.fold(
                error -> { throw new DomainException(Vector.of(error), RECEIPT_ORDER_URI); },
                detail -> ResponseEntity.ok(ReceiptOrderDetailResponse.of(detail))
        );
    }
}
