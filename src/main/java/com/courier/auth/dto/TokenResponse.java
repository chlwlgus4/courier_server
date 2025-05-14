package com.courier.auth.dto;

public record TokenResponse(
        String tokenType,
        String accessToken,
        String refreshToken
) {}
