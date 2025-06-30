package com.courier.orders.dto;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderImageResponse(
        Long id,
        String imagePath,
        String originalFilename,
        Long fileSize,
        String contentType,
        Integer imageOrder,
        String base64Data,  // Base64로 인코딩된 이미지 데이터
        LocalDateTime createdDate
) {
}
