package com.courier.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok().build();
    }
}
