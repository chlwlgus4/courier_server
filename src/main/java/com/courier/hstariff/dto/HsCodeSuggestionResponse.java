package com.courier.hstariff.dto;

import lombok.Builder;

@Builder
public record HsCodeSuggestionResponse(
        String hsCode,
        String koreanName,
        String englishName
) {
}
