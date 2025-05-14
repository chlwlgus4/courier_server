package com.courier.handler.dto;

import com.courier.handler.exception.ErrorCode;

public record ErrorDTO(
        ErrorCode code,
        String message,
        String path
)
{}
