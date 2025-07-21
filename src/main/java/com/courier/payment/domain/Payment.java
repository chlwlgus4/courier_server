package com.courier.payment.domain;

import com.courier.orders.domain.Orders;
import com.courier.payment.enums.PaymentStatus;
import com.courier.payment.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(length = 20)
    private String version;

    @Column(name = "payment_key", length = 200, nullable = false, unique = true)
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentType type;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_name", length = 255)
    private String orderName;

    @Column(name = "mid", length = 50)
    private String mid;

    @Column(length = 5)
    private String currency;

    @Column(length = 50)
    private String method;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "balance_amount", precision = 15, scale = 2)
    private BigDecimal balanceAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus status;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "last_transaction_key", length = 100)
    private String lastTransactionKey;

    @Column(name = "supplied_amount", precision = 15, scale = 2)
    private BigDecimal suppliedAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal vat;

    @Column(name = "culture_expense")
    @Builder.Default
    private Boolean cultureExpense = false;

    @Column(name = "tax_free_amount", precision = 15, scale = 2)
    private BigDecimal taxFreeAmount;

    @Column(name = "tax_exemption_amount", precision = 15, scale = 2)
    private BigDecimal taxExemptionAmount;

    @Column(name = "use_escrow")
    @Builder.Default
    private Boolean useEscrow = false;

    @Column(length = 5)
    private String country;

    @Column(name = "is_partial_cancelable")
    @Builder.Default
    private Boolean isPartialCancelable = true;

    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;

    @Column(name = "checkout_url", length = 500)
    private String checkoutUrl;

    // Card 정보
    @Column(name = "card_issuer_code", length = 10)
    private String cardIssuerCode;

    @Column(name = "card_acquirer_code", length = 10)
    private String cardAcquirerCode;

    @Column(name = "card_number", length = 20)
    private String cardNumber;

    @Column(name = "card_installment_plan_months")
    private Integer cardInstallmentPlanMonths;

    @Column(name = "card_is_interest_free")
    private Boolean cardIsInterestFree;

    @Column(name = "card_approve_no", length = 20)
    private String cardApproveNo;

    @Column(name = "card_use_card_point")
    private Boolean cardUseCardPoint;

    @Column(name = "card_type", length = 20)
    private String cardType;

    @Column(name = "card_owner_type", length = 20)
    private String cardOwnerType;

    @Column(name = "card_acquire_status", length = 20)
    private String cardAcquireStatus;

    @Column(name = "card_amount", precision = 15, scale = 2)
    private BigDecimal cardAmount;

    // EasyPay 정보
    @Column(name = "easy_pay_provider", length = 50)
    private String easyPayProvider;

    @Column(name = "easy_pay_amount", precision = 15, scale = 2)
    private BigDecimal easyPayAmount;

    @Column(name = "easy_pay_discount_amount", precision = 15, scale = 2)
    private BigDecimal easyPayDiscountAmount;

    // 실패 정보
    @Column(name = "failure_code", length = 50)
    private String failureCode;

    @Column(name = "failure_message", length = 500)
    private String failureMessage;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Orders order;

    @ToString.Exclude
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PaymentCancel> cancels = new ArrayList<>();


}
