package com.courier.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_cancels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "cancel_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal cancelAmount;

    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    @Column(name = "tax_free_amount", precision = 15, scale = 2)
    private BigDecimal taxFreeAmount;

    @Column(name = "tax_exemption_amount", precision = 15, scale = 2)
    private BigDecimal taxExemptionAmount;

    @Column(name = "refundable_amount", precision = 15, scale = 2)
    private BigDecimal refundableAmount;

    @Column(name = "transfer_discount_amount", precision = 15, scale = 2)
    private BigDecimal transferDiscountAmount;

    @Column(name = "easy_pay_discount_amount", precision = 15, scale = 2)
    private BigDecimal easyPayDiscountAmount;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "transaction_key", length = 64, nullable = false)
    private String transactionKey;

    @Column(name = "receipt_key", length = 200)
    private String receiptKey;

    @Column(name = "cancel_status", length = 20, nullable = false)
    private String cancelStatus;

    @Column(name = "cancel_request_id", length = 100)
    private String cancelRequestId;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    private Payment payment;

}
