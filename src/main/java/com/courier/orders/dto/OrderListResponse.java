package com.courier.orders.dto;

import java.util.List;

public record OrderListResponse(
        List<OrderGetResponse> orders,
        boolean hasNext,
        int totalElements,
        int page,
        int size
) {
}
