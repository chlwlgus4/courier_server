package com.courier.auth.dto;

import com.courier.user.domain.User;

public record AuthResponse(UserResponseDTO user, String accessToken) {

    public static AuthResponse of(User user, String accessToken) {
        return new AuthResponse(UserResponseDTO.from(user), accessToken);
    }

}
