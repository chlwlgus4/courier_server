package com.courier.hstariff.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hs_tariff",
        uniqueConstraints = @UniqueConstraint(columnNames = {"hsCode", "startDate"}),
        indexes = @Index(name = "idx_hs_code", columnList = "hsCode"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HsTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hs_code", nullable = false)
    private String hsCode;

    @Column(name = "korean_name", columnDefinition = "TEXT")
    private String koreanName;

    @Column(name = "english_name", columnDefinition = "TEXT")
    private String englishName;

    @Column(name = "duty_rate", precision = 38, scale = 2)
    private BigDecimal dutyRate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "unit_tax", precision = 38, scale = 2)
    private BigDecimal unitTax;

    @Column(name = "reference_price", precision = 38, scale = 2)
    private BigDecimal referencePrice;

    @Column(name = "use_type_code")
    private String useTypeCode;

    @Column(columnDefinition = "LONGTEXT")
    private String notes;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}

