package com.courier.hstariff.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hs_tariff", uniqueConstraints = @UniqueConstraint(columnNames = {"hsCode", "startDate"}))
public class HsTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hsCode;

    @Column(columnDefinition = "TEXT")
    private String koreanName;

    @Column(columnDefinition = "TEXT")
    private String englishName;

    private BigDecimal dutyRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal unitTax;
    private BigDecimal referencePrice;
    private String useTypeCode;

    @Lob
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
