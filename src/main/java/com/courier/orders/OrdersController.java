package com.courier.orders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok().build();
    }
}
