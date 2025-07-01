package com.courier.orders;

import com.courier.orders.dto.OrderGetResponse;
import com.courier.orders.dto.OrderListResponse;
import com.courier.orders.dto.OrderSaveRequest;
import com.courier.orders.dto.OrderSaveResponse;
import com.courier.orders.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping
    public ResponseEntity<OrderListResponse> getOrders(@PageableDefault(size=20, sort="createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                       @RequestParam(required = false) String status,
                                                       @RequestParam(required = false) String search,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        OrderStatus orderStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                orderStatus = OrderStatus.fromString(status);
            } catch (IllegalArgumentException e) {
                // 잘못된 status 값이면 무시하거나 예외 처리
                orderStatus = null;
            }
        }

        OrderListResponse orders = ordersService.getOrders(pageable, orderStatus, search, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderGetResponse> getOrder(@PathVariable Long id) {
        OrderGetResponse order = ordersService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderSaveResponse> saveOrder(@ModelAttribute OrderSaveRequest dto) {
        Long id = ordersService.save(dto);
        return ResponseEntity.ok(new OrderSaveResponse(id));
    }
}
