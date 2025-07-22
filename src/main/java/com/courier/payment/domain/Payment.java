package com.courier.payment.domain;

import com.courier.orders.domain.Orders;
import com.courier.payment.enums.PaymentStatus;
import com.courier.payment.enums.PaymentType;
import com.courier.payment.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(length = 11)
    private String version;

    @Column(name = "payment_key", nullable = false, length = 200, unique = true)
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "order_name", length = 100)
    private String orderName;

    @Column(name = "m_id", length = 14)
    private String mId;

    @Column(length = 3)
    private String currency;

    @Column(length = 50)
    private String method;

    @Column(name = "totalAmount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "approve_date")
    private LocalDateTime approveDate;

    @Column(name = "last_transaction_key", length = 64)
    private String lastTransactionKey;

    @Column(name = "supplied_amount", precision = 10, scale = 2)
    private BigDecimal suppliedAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal vat;

    @Column(length = 50)
    private String secret;

    @Column(name = "customer_mobile_phone", length = 15)
    private String customerMobilePhone;

    @Enumerated(EnumType.STRING)
    private SettlementStatus mobilePhoneSettlementStatus;

    @Column(name = "transfer_bank_code", length = 10)
    private String transferBankCode;

    @Enumerated(EnumType.STRING)
    private SettlementStatus transferSettlementStatus;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Orders order;

    @ToString.Exclude
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PaymentCancel> cancels = new ArrayList<>();


}
