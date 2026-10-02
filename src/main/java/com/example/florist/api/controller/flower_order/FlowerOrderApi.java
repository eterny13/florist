package com.example.florist.api.controller.flower_order;

import com.example.florist.api.controller.flower_order.request.FlowerOrderRequest;
import com.example.florist.domain.flower.FlowerOrderDetail;
import com.example.florist.domain.shared.DomainError;
import com.example.florist.service.flower_order.FlowerOrderService;
import io.vavr.collection.Vector;
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
import java.util.List;

@RestController
@RequestMapping("/flower-orders")
@RequiredArgsConstructor
public class FlowerOrderApi {
    private static final URI FLOWER_ORDERS_URI = URI.create("/flower-orders");
    private final FlowerOrderService flowerOrderService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody List<FlowerOrderRequest> requests) {
        Vector<Either<DomainError, FlowerOrderDetail>> results = Vector.ofAll(requests)
                .map(r -> flowerOrderService.order(r.flowerCode(), r.quantity()));

        Vector<DomainError> errors = results.filter(Either::isLeft).map(Either::getLeft);
        if (errors.nonEmpty()) {
            String errorMsg = errors.map(Object::toString).mkString(", ");
            var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setType(FLOWER_ORDERS_URI);
            problemDetail.setDetail(errorMsg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Flower Order Success");
    }
}
