package com.courier.hstariff.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class HsTariffDTO {
    private String hsCode;
    private String koreanName;
    private String englishName;
    private BigDecimal dutyRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal unitTax;
    private BigDecimal referencePrice;
    private String useTypeCode;
    private String notes;

}

