package com.courier.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class PaymentConfirmResponse {
    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private Integer taxExemptionAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private Boolean useEscrow;
    private Boolean cultureExpense;
    private Card card;
    private EasyPay easyPay;
    private String country;
    private Boolean isPartialCancelable;
    private Receipt receipt;
    private Checkout checkout;
    private String currency;
    private Long totalAmount;
    private Long balanceAmount;
    private Long suppliedAmount;
    private Long vat;
    private Long taxFreeAmount;
    private String method;
    private String version;
    private String type;

    @Getter
    public static class Card {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanMonths;
        private Boolean isInterestFree;
        private String interestPayer;
        private String approveNo;
        private Boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private Long amount;
    }

    @Getter
    public static class EasyPay {
        private String provider;
        private Long amount;
        private Long discountAmount;
    }

    @Getter
    public static class Receipt {
        private String url;
    }

    @Getter
    public static class Checkout {
        private String url;
    }
}
