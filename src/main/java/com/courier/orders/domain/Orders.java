package com.courier.orders.domain;


import com.courier.orders.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "shipping_type_code", length = 20, nullable = false)
    private String shippingTypeCode;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal weight;

    @Column(name = "insurance_value", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal insuranceValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED')")
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "origin_country", length = 50)
    @Builder.Default
    private String originCountry = "미국";

    @Column(name = "destination_country", length = 50)
    @Builder.Default
    private String destinationCountry = "한국";

    @Column(name = "origin_postal_code", length = 20)
    private String originPostalCode;

    @Column(name = "origin_address", length = 500)
    private String originAddress;

    @Column(name = "origin_address_detail", length = 200)
    private String originAddressDetail;

    @Column(name = "destination_postal_code", length = 20)
    private String destinationPostalCode;

    @Column(name = "destination_address", length = 500)
    private String destinationAddress;

    @Column(name = "destination_address_detail", length = 200)
    private String destinationAddressDetail;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_date", updatable = false)
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    @Builder.Default
    private LocalDateTime updatedDate = LocalDateTime.now();

    @Column(name = "courier_id")
    private Integer courierId;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("imageOrder ASC")
    @Builder.Default
    @JsonManagedReference
    private List<OrderImage> orderImages = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

}
