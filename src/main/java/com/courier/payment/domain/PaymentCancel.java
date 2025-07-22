package com.courier.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "transaction_key", length = 64, unique = true)
    private String transactionKey;

    private Long orderId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 200)
    private String reason;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    private String status;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    private Payment payment;

}
