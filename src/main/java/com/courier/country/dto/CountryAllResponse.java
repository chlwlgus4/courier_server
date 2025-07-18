package com.courier.country.dto;

import lombok.Builder;

@Builder
public record CountryAllResponse(
        Long id,
        String nameKo,
        String nameEn,
        String callingCode
) {
}
