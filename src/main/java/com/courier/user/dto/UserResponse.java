package com.courier.user.dto;

import com.courier.user.domain.User;

public record UserResponse(UserResponseDTO user) {
    public static UserResponse of(User user) {
        return new UserResponse(UserResponseDTO.from(user));
    }
}
