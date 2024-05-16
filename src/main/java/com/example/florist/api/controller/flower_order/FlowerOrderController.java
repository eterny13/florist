package com.example.florist.api.controller.flower_order;

import com.example.florist.api.controller.flower_order.request.FlowerOrderRequest;
import com.example.florist.service.flower_order.FlowerOrderService;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FlowerOrderController {
    private final FlowerOrderService flowerOrderService;

    @RequestMapping(value = "/flower-orders", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody List<FlowerOrderRequest> requests) {
        requests.forEach(x ->
                flowerOrderService.order(x.flowerCode(), x.quantity())
        );
        return "Order Success";
    }
}
