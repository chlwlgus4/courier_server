package com.courier.orders.dto;

import com.courier.orders.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderGetResponse(
        String shippingType,
        BigDecimal weight,
        BigDecimal insuranceValue,
        OrderStatus status,
        String originCountry,
        String destinationCountry,
        String originPostalCode,
        String originAddress,
        String originAddressDetail,
        String destinationPostalCode,
        String destinationAddress,
        String destinationAddressDetail,
        String notes,
        List<OrderImageResponse> images
) {
}
