package com.courier.payment.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_card_info")
public class PaymentCardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "issuer_code", length = 2)
    private String issuerCode;

    @Column(length = 20)
    private String number;

    @Column(name = "installment_plan_months")
    private Integer installmentPlanMonths;

    @Column(name = "approve_no", length = 8)
    private String approveNo;

    @Column(length = 20)
    private String type;

    @Column(length = 20)
    private String ownerType;

    @Column(name = "is_interest_free")
    private Boolean isInterestFree;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
