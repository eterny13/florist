package com.example.florist.api.controller.flower_order;

import com.example.florist.api.controller.flower_order.request.FlowerOrderRequest;
import com.example.florist.service.flower_order.FlowerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlowerOrderController {
    private final FlowerOrderService flowerOrderService;

    @RequestMapping(value = "/flower-orders", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> post(@RequestBody List<FlowerOrderRequest> requests) {
        requests.forEach(x ->
                flowerOrderService.order(x.flowerCode(), x.quantity())
        );
        return ResponseEntity.ok("Flower Order Success");
    }
}
