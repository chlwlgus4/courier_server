package com.courier.orders.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSaveRequest {
    private String shippingTypeCode;
    private BigDecimal weight;
    private BigDecimal insuranceValue;

    private String originCountry;
    private String originPostalCode;
    private String originAddress;
    private String originAddressDetail;

    private String destinationCountry;
    private String destinationPostalCode;
    private String destinationAddress;
    private String destinationAddressDetail;

    private String notes;

    private List<MultipartFile> images;
}
