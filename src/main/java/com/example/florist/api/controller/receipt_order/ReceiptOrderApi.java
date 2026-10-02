package com.example.florist.api.controller.receipt_order;

import com.example.florist.api.controller.receipt_order.request.ReceiptOrderDetailRequest;
import com.example.florist.api.controller.receipt_order.response.ReceiptOrderDetailResponse;
import com.example.florist.domain.receipt_order.ReceiptOrderDetail;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.service.receipt_order.ReceiptOrderService;
import io.vavr.control.Either;
import io.vavr.control.Option;
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
@RequestMapping("/receipt-order")
@RequiredArgsConstructor
public class ReceiptOrderApi {
    private static final URI RECEIPT_ORDER_URI = URI.create("/receipt-order");
    private final ReceiptOrderService receiptOrderService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody ReceiptOrderDetailRequest request) {
        Either<DomainError, ReceiptOrderDetail> result = receiptOrderService.receive(
                request.customerId(),
                request.deliveryDate(),
                request.deliveryAddress(),
                request.recipientName(),
                request.bouquetId(),
                Option.of(request.deliveryMessage()),
                request.recipientPhoneNumber()
        );

        if (result.isLeft()) {
            DomainError error = result.getLeft();
            HttpStatus status = (error instanceof DomainError.NotFoundError) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            var problemDetail = ProblemDetail.forStatus(status);
            problemDetail.setType(RECEIPT_ORDER_URI);

            if (error instanceof DomainError.OutOfStockError outOfStock) {
                problemDetail.setDetail("Out of Stock: " + outOfStock.flower().getName()
                        + " (Required: " + outOfStock.requested().value()
                        + ", Available: " + outOfStock.available().value() + ")");
            } else if (error instanceof DomainError.ValidationError ve) {
                problemDetail.setDetail("Validation Error: " + ve.message());
            } else if (error instanceof DomainError.NotFoundError nf) {
                problemDetail.setDetail("Not Found Error: " + nf.message());
            } else {
                problemDetail.setDetail(error.toString());
            }

            return ResponseEntity.status(status).body(problemDetail);
        }

        return ResponseEntity.ok(ReceiptOrderDetailResponse.of(result.get()));
    }
}
