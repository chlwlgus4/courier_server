package com.courier.payment.dto;

import lombok.Builder;

@Builder
public record PaymentConfirmResponse(
        boolean result
) {
}
