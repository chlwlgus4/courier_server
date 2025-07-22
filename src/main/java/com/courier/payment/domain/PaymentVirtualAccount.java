package com.courier.payment.domain;

import com.courier.payment.enums.RefundStatus;
import com.courier.payment.enums.SettlementStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_virtual_account")
public class PaymentVirtualAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "account_type", length = 20)
    private String accountType;

    @Column(length = 20)
    private String number;

    @Column(name = "bank_code", length = 10)
    private String bankCode;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "depositor_name", length = 100)
    private String depositorName;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    @Column(name = "expired")
    private Boolean expired;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus;

    @Column(name = "refund_bank_code", length = 10)
    private String refundBankCode;

    @Column(name = "refund_account_number", length = 20)
    private String refundAccountNumber;

    @Column(name = "refund_holder_name", length = 100)
    private String refundHolderName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
