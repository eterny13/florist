package com.example.florist.api.controller.receipt_order;

import com.example.florist.api.controller.receipt_order.request.ReceiptOrderDetailRequest;
import com.example.florist.api.controller.receipt_order.response.ReceiptOrderDetailResponse;
import com.example.florist.service.receipt_order.ReceiptOrderService;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReceiptOrderController {
    private final ReceiptOrderService receiptOrderService;

    @RequestMapping(value = "/receipt-order", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> post(@RequestBody ReceiptOrderDetailRequest request) {
        var receiptOrderDetail = receiptOrderService.receive(
                request.customerId(),
                request.deliveryDate(),
                request.deliveryAddress(),
                request.recipientName(),
                request.bouquetId(),
                Option.of(request.deliveryMessage()),
                request.recipientPhoneNumber()
        );
        return ResponseEntity.ok(ReceiptOrderDetailResponse.of(receiptOrderDetail));
    }
}
