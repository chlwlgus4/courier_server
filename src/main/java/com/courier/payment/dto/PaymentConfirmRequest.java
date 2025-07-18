package com.courier.payment.dto;

public record PaymentConfirmRequest(
        String orderId,
        String amount,
        String paymentKey
) {
}
