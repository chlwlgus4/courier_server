package com.courier.orders;

import com.courier.orders.domain.Orders;
import com.courier.orders.dto.OrderGetResponse;
import com.courier.orders.dto.OrderSaveRequest;
import com.courier.orders.dto.OrderSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("{id}")
    public ResponseEntity<?> getOrders(@PathVariable Long id) {
        OrderGetResponse order = ordersService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderSaveResponse> saveOrder(@ModelAttribute OrderSaveRequest dto) {
        Long id = ordersService.save(dto);
        return ResponseEntity.ok(new OrderSaveResponse(id));
    }
}
